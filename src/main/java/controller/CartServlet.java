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

        // Chuyển tiếp đến trang giỏ hàng
        request.getRequestDispatcher(config.Config.JSPMapper.CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productItemId = request.getParameter("productItemId") == null ? null : Integer.parseInt(request.getParameter("productItemId"));
        Integer quantity = request.getParameter("quantity") == null ? null : Integer.parseInt(request.getParameter("quantity"));
        model.User user = (model.User) request.getSession(false).getAttribute("user");

        // Kiểm tra dữ liệu yêu cầu
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

            model.CartItem cartItem = new model.CartItem();
            cartItem.setProductItemId(productItem);
            cartItem.setCartId(cart);
            cartItem.setQuantity(quantity);

            dao.CartItemDAO.CartItemManager.createCartItem(cartItem);

            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO ADD ITEM TO CART, REASON: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add item to cart");
        }
    }
}