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
        String[] notificationIds = request.getParameterValues("notificationIds");

        if (notificationIds == null || notificationIds.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing notificationIds");
            return;
        }

        try {
            for (String idStr : notificationIds) {
                int notificationId = Integer.parseInt(idStr);
                model.Notification notification = new model.Notification();
                notification.setId(notificationId);
                dao.NotificationDAO.NotificationManager.markAsRead(notification);
            }

            response.sendRedirect(request.getContextPath() + "/notification");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid notificationId");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error marking notifications as read.");
        }
    }
}
