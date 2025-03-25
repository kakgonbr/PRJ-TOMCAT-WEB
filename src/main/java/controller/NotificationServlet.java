/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 *
 * @author hoahtm
 */
public class NotificationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");

        try {
            java.util.List<model.Notification> notifications = dao.NotificationDAO.NotificationFetcher.fetchFor(user.getId());
            request.setAttribute("notification", notifications);
            request.getRequestDispatcher(config.Config.JSPMapper.NOTIFICATION).forward(request, response);
        } catch (Exception e) {
            service.Logging.logger.error("Error processing edit action: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String[] notificationIds = request.getParameterValues("notificationIds");

    if (notificationIds == null || notificationIds.length == 0) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing notificationIds");
        return;
    }

    java.util.List<java.util.Map<String, Object>> jsonResponse = new java.util.ArrayList<>();  // Danh sách kết quả thay vì JSONArray

    try {
        for (String idStr : notificationIds) {
            int notificationId = Integer.parseInt(idStr);
            model.Notification notification = new model.Notification();
            notification.setId(notificationId);

            dao.NotificationDAO.NotificationManager.markAsRead(notification);

            java.util.Map<String, Object> notificationData = new java.util.HashMap<>();
            notificationData.put("notificationId", notificationId);
            notificationData.put("success", true);

            jsonResponse.add(notificationData);
        }

        com.google.gson.Gson gson = new GsonBuilder().create();
        PrintWriter out = response.getWriter();
        out.write(gson.toJson(jsonResponse));  // Trả về danh sách JSON
        out.flush();

    } catch (NumberFormatException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid notificationId");
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error marking notifications as read.");
    }
}
}
