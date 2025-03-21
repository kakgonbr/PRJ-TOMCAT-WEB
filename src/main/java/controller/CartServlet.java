package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // should have made cart items reference user instead

            model.User user = (model.User)request.getSession(false).getAttribute("user");

            model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId(), true);

            if (cart == null) {
                cart = new model.Cart();
                cart.setCartItemList(new java.util.ArrayList<>());

                cart.setUserId(user);

                dao.CartDAO.CartManager.createCart(cart);
            }

            request.setAttribute("cartItems", cart.getCartItemList().stream().map(model.CartItemWrapper::new).toList());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET CART ITEMS, REASON: {}", e.getMessage());
            request.setAttribute("code", 500);
            request.getRequestDispatcher("/error").forward(request, response);

            return;
        }

        request.getRequestDispatcher(config.Config.JSPMapper.CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productItemId = request.getParameter("productItemId") == null ? null : Integer.parseInt(request.getParameter("productItemId"));
        Integer quantity = request.getParameter("quantity") == null ? null : Integer.parseInt(request.getParameter("quantity"));
        model.User user = (model.User) request.getSession(false).getAttribute("user");

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");

        switch (action) {
            case "update":
                updateQuantity(request, response, productItemId, quantity, user);
            break;
            case "remove":
                removeFromCart(request, response, productItemId, quantity, user);
            break;
            default:
                addToCart(request, response, productItemId, quantity, user);
        }
    }

    private static void updateQuantity(HttpServletRequest request, HttpServletResponse response, Integer cartItemId, Integer quantity, model.User user) throws ServletException, IOException {
        if (quantity == null || quantity == 0) {
            removeFromCart(request, response, cartItemId, quantity, user);
        }
        try {
            model.CartItem cartItem = dao.CartItemDAO.CartItemManager.getCartItem(cartItemId);
            
            if (cartItem.getProductItemId().getStock() < quantity) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity cannot exceed stock");
                return;
            }
            
            cartItem.setQuantity(quantity);
            dao.CartItemDAO.CartItemManager.updateCartItem(cartItem);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO UPDATE QUANTITY, REASON: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update quantity");
        }
    }

    private static void removeFromCart(HttpServletRequest request, HttpServletResponse response, Integer cartItemId, Integer quantity, model.User user) throws ServletException, IOException {
        try {
            model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId(), false);

            cart.getCartItemList().removeIf(ci -> ci.getId() == cartItemId);

            dao.CartDAO.CartManager.updateCart(cart);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO REMOVE ITEM FROM CART, REASON: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to remove item from cart");
        }
    }

    private static void addToCart(HttpServletRequest request, HttpServletResponse response, Integer productItemId, Integer quantity, model.User user) throws ServletException, IOException {
        if (productItemId == null || quantity == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }

        try {
            model.ProductItem productItem = dao.ProductDAO.ProductFetcher.getProductItem(productItemId);

            if (productItem.getStock() < quantity) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity cannot exceed stock");
                return;
            }

            model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId(), false);

            Integer preexistingItem = null;
            for (final model.CartItem item : cart.getCartItemList()) {
                if (item.getProductItemId().getId() == productItem.getId()) {
                    preexistingItem = item.getId();

                    break;
                }
            }

            model.CartItem cartItem;
            if (preexistingItem == null) {
                cartItem = new model.CartItem();
                cartItem.setProductItemId(productItem);
                cartItem.setCartId(cart);
                cartItem.setQuantity(quantity);
                dao.CartItemDAO.CartItemManager.createCartItem(cartItem);
            } else {
                cartItem = dao.CartItemDAO.CartItemManager.getCartItem(preexistingItem);
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                
                if (cartItem.getQuantity() > productItem.getStock()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity cannot exceed stock");
                    return;
                }

                dao.CartItemDAO.CartItemManager.updateCartItem(cartItem);
            }

            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO ADD ITEM TO CART, REASON: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add item to cart");
        }
    }
}
