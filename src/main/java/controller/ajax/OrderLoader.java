/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ajax;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author hoahtm
 */
public class OrderLoader extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String shopIdStr = request.getParameter("shopId");
        System.out.println("DEBUG: Received shopId = " + shopIdStr);

        if (shopIdStr == null || shopIdStr.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing shopId");
            return;
        }

        try {
            int shopId = Integer.parseInt(shopIdStr);
            System.out.println("DEBUG: Parsed shopId = " + shopId);

            java.util.List<model.dto.OrderedItemDTO> orderItems = dao.OrderDAO.OrderManager.getOrderItemsByShop(shopId);

            System.out.println("DEBUG: Returning " + orderItems.size() + " items.");
            String json = new com.google.gson.Gson().toJson(orderItems);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid shopId");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching order items: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
