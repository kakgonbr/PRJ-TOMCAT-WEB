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

            model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId());

            if (cart == null) {
                cart = new model.Cart();

                cart.setUserId(user);

                dao.CartDAO.CartManager.createCart(cart);
            }

            request.setAttribute("cartItems", cart.getCartItemList().stream().map(model.CartItemWrapper::new).toList());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET CART ITEMS, REASON: {}", e.getMessage());
            request.setAttribute("code", 404);
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

            model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser(user.getId());

            model.CartItem cartItem = new model.CartItem();
            cartItem.setProductItemId(productItem);
            cartItem.setCartId(cart);
            cartItem.setQuantity(quantity);

            dao.CartItemDAO.CartItemManager.createCartItem(cartItem);
        } catch (java.sql.SQLException e) {

        }
    }
}
