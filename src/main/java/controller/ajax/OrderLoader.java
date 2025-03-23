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
        System.out.println("DEBUG: Received shopId = " + shopIdStr); // Debug đầu vào

        if (shopIdStr == null || shopIdStr.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing shopId");
            return;
        }

        try {
            int shopId = Integer.parseInt(shopIdStr);
            System.out.println("DEBUG: Parsed shopId = " + shopId); // Debug sau khi parse

            java.util.List<Object[]> rawData = dao.OrderDAO.OrderManager.getOrderItemsByShop(shopId);

            java.util.List<model.dto.OrderedItemDTO> orderItems = new java.util.ArrayList<>();
            for (Object[] row : rawData) {
                model.dto.OrderedItemDTO dto = new model.dto.OrderedItemDTO(
                        row[0] != null ? (String) row[0] : "", // userName
                        row[1] != null ? (String) row[1] : "", // productName
                        row[2] != null ? (Double) row[2] : 0.0, // totalPrice
                        row[3] != null ? (java.util.Date) row[3] : new java.util.Date(), // date
                        row[4] != null ? (Double) row[4] : 0.0 // shippingCost
                );
                orderItems.add(dto);
            }

            System.out.println("DEBUG: Returning " + orderItems.size() + " items."); // Debug số lượng items
            String json = new com.google.gson.Gson().toJson(orderItems);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid shopId");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching order items");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
