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
            request.setAttribute("cartItems", dao.CartDAO.CartFetcher.getCartByUser(user.getId(), true).getCartItemList().stream().map(model.CartItemWrapper::new).toList());
            request.setAttribute("promotions", dao.PromotionDAO.PromotionFetcher.checkAvailablePromotions(user.getId()).stream().map(model.PromotionWrapper::new).toList());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET AVAILABLE PROMOTIONS FOR USER {}", user.getId());
        }

        request.getRequestDispatcher(config.Config.JSPMapper.CHECKOUT).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        model.User user = (model.User) request.getSession(false).getAttribute("user");
        Integer promotionId = request.getParameter("promotionId") == null ? null : Integer.parseInt(request.getParameter("promotionId"));

        if (action == null || action.isBlank()) {
            doGet(request, response);
            return;
        }

        try {
            switch (action) {
                case "proceed":
                    int order = dao.OrderDAO.OrderManager.createOrder(new model.ProductOrder());

                    model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId(), true);

                    model.Promotion promotion = null;
                    if (promotionId != null) {
                        promotion = dao.PromotionDAO.PromotionFetcher.getPromotion(promotionId);
                    }

                    dao.OrderDAO.OrderedItemManager.transferFromCart(order, cart.getCartItemList(), promotion);

                    dao.OrderDAO.OrderManager.updatePrice(order);

                    model.ProductOrder dbOrder = dao.OrderDAO.OrderManager.getOrder(order);

                    response.sendRedirect(service.vnpay.PortalService.getLink(request.getRemoteAddr(), "", dbOrder.getFinalPrice().longValue() * 100, "vn", Integer.toString(order)));
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
            service.Logging.logger.info("Action {} on cart by user {} resulted in {}", action, user.getId(), e.getMessage());
            request.setAttribute("error", "Something went wrong, try again.");
        } 
        doGet(request, response);
    }
}