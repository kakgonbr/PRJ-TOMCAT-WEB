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
            // Lấy giỏ hàng của người dùng từ session
            model.User user = (model.User) request.getSession(false).getAttribute("user");
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User  not logged in");
                return;
            }

            // Lấy danh sách các mục trong giỏ hàng
            request.setAttribute("cartItems",
                    dao.CartDAO.CartFetcher.getCart(user.getId()).getCartItemList().stream()
                            .map(model.CartItemWrapper::new)
                            .toList()
            );
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET CART ITEMS, REASON: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve cart items");
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
            // Lấy sản phẩm từ cơ sở dữ liệu
            model.ProductItem productItem = dao.ProductDAO.ProductFetcher.getProductItem(productItemId);

            // Kiểm tra số lượng sản phẩm có trong kho
            if (productItem.getStock() < quantity) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity cannot exceed stock");
                return;
            }

            // Lấy giỏ hàng của người dùng
            model.Cart cart = dao.CartDAO.CartFetcher.getCartByUser (user.getId());

            // Tạo một mục giỏ hàng mới
            model.CartItem cartItem = new model.CartItem();
            cartItem.setProductItemId(productItem);
            cartItem.setCartId(cart);
            cartItem.setQuantity(quantity);

            // Thêm mục giỏ hàng vào cơ sở dữ liệu
            //dao.CartItemDAO.CartItemManager.createCartItem(cartItem);

            // Chuyển hướng về trang giỏ hàng
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO ADD ITEM TO CART, REASON: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add item to cart");
        }
    }
}