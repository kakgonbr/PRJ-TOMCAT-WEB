package controller;

import dao.CartItemDAO;
import model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CartItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch all cart items using the DAO method
            List<CartItem> cartItems = CartItemDAO.CartItemManager.getAllCartItems();
            request.setAttribute("cartItems", cartItems);

            // Forward to the JSP page to display the items
            request.getRequestDispatcher("viewCart.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch cart items.");
        }
    }
}