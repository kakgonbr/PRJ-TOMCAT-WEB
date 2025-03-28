package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This servlet is not very optimized.
 */
// TODO: ADD DISTANCE CALCULATION? MAYBE?
public class CheckOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.User user = (model.User) request.getSession(false).getAttribute("user");

        
        try {
            java.util.List<model.CartItemWrapper> cartItems = dao.CartDAO.CartFetcher.getCartByUser(user.getId(), true).getCartItemList().stream().map(model.CartItemWrapper::new).toList();
            request.setAttribute("cartItems", cartItems);

            if (checkExceed(request, response, cartItems)) {
                return;
            }

            var promotions = dao.PromotionDAO.PromotionFetcher.checkAvailablePromotions(user.getId()).stream().map(model.PromotionWrapper::new).toList();

            service.Logging.logger.info("Promotions found for user {}: {}", user.getId(), promotions.size());

            request.setAttribute("promotions", promotions);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET AVAILABLE PROMOTIONS FOR USER {}", user.getId());
        }

        request.getRequestDispatcher(config.Config.JSPMapper.CHECKOUT).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        model.User user = (model.User) request.getSession(false).getAttribute("user");
        Integer promotionId = request.getParameter("promotionId") == null || request.getParameter("promotionId").isBlank() ? null : Integer.parseInt(request.getParameter("promotionId"));

        if (action == null || action.isBlank()) {
            doGet(request, response);
            return;
        }

        // the entity manager is controlled at the data access level, so is the transaction.
        // if anything here breaks mid way through, there isn't a quick way to roll back.
        // terrible architectural decision if you ask me
        model.ProductOrder order = null;
        try {
            switch (action) {
                case "proceed":
                    // what a mess
                    model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId(), true);

                    String shippingAddress = request.getParameter("address");
                    if (shippingAddress == null || shippingAddress.isBlank()) {
                        request.setAttribute("error", "Shipping address is required");
                        doGet(request, response);
                        return;
                    }

                    String shippingCostsJson = request.getParameter("shippingCosts");
                    java.util.Map<String, Double> shippingCosts = new com.google.gson.Gson().fromJson(shippingCostsJson, new com.google.gson.reflect.TypeToken<java.util.Map<String, Double>>(){}.getType());

                    if (checkExceed(request, response, cart.getCartItemList().stream().map(model.CartItemWrapper::new).toList())) {
                        return;
                    }

                    model.Promotion promotion = null;
                    if (promotionId != null) {
                        promotion = dao.PromotionDAO.PromotionFetcher.getPromotion(promotionId);
                    }
                    order = new model.ProductOrder();
                    order.setUserId(user);
                    order.setDate(new java.util.Date());
                    order.setPromotionId(promotion);
                    order.setStatus(false);
                    order.setShippingAddress(shippingAddress);
                    int orderId = dao.OrderDAO.OrderManager.createOrder(order);

                    // the moment this goes through, item stock will be deducted
                    dao.OrderDAO.OrderedItemManager.transferFromCart(orderId, cart.getCartItemList().stream().map(model.CartItemWrapper::new).toList(), shippingCosts);

                    dao.OrderDAO.OrderManager.updatePrice(orderId);

                    model.ProductOrder dbOrder = dao.OrderDAO.OrderManager.getOrder(orderId);

                    response.sendRedirect(service.vnpay.PortalService.getLink(request.getRemoteAddr(), "", dbOrder.getFinalPrice().longValue() * 100, "vn", Integer.toString(orderId)));

                    // messy, but vnpay doesnt always call the ipn servlet when the transaction fails
                    listeners.SchedulerListener.getScheduler().schedule(() -> {
                        try {
                            service.OrderConcurrencyService.checkAndRemove(orderId);
                        } catch (java.sql.SQLException e) {
                            service.Logging.logger.error("SCHEDULED ORDER CHECK JOB FAILED, REASON: {}", e.getMessage());
                        }
                    }, 6, java.util.concurrent.TimeUnit.MINUTES);
                    return;
                case "apply":
    
                    // only way we can tell if the user has modified the promotion on client
                    if (promotionId != null && dao.PromotionDAO.PromotionFetcher.checkPromotionUsage(user.getId(), promotionId) == null) {
                        // doesnt get applied here, let the client send it to the proceed case then apply
                        request.setAttribute("activePromotion", dao.PromotionDAO.PromotionFetcher.getPromotion(promotionId));
                    }
                break;
            }
        } catch (java.sql.SQLException e) {
            // this is just awful
            if (order != null) {
                try {
                    service.OrderConcurrencyService.removeFromOrder(order.getId());
                    dao.OrderDAO.OrderManager.deleteOrder(order.getId());
                } catch (java.sql.SQLException e1) {
                }
            }
            service.Logging.logger.info("Action {} on cart by user {} resulted in {}", action, user.getId(), e.getMessage());
            request.setAttribute("error", "Something went wrong, try again.");
        } 
        doGet(request, response);
    }

    private static boolean checkExceed(HttpServletRequest request, HttpServletResponse response, java.util.List<model.CartItemWrapper> cartItems) throws ServletException, IOException {
        java.util.List<model.CartItemWrapper> exceedStock = new java.util.ArrayList<>();
        for (final model.CartItemWrapper cartItem : cartItems) {
            if (cartItem.getQuantity() > cartItem.getProductItem().getStock()) {
                exceedStock.add(cartItem);
            }
        }

        if (exceedStock.size() > 0) {
            request.setAttribute("exceed", exceedStock);

            request.getRequestDispatcher(config.Config.JSPMapper.EXCEED_STOCK).forward(request, response);

            return true;
        }

        return false;
    }
}