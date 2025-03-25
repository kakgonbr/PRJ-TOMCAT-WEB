package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Mapped to /admin
 */
public class AdminServlet extends HttpServlet {
/**
     * This methods expects the authentication has already been done by the filter
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("averageResponse", dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.getAverageResponseTime());
        request.setAttribute("maxResponse", dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.getMaxResponseTime());
        request.setAttribute("currentSessions", listeners.SessionCountingListener.getSessionCount());
        request.setAttribute("peakSessions", dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.getPeakSession());
        request.setAttribute("visits", dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.getVisits());

        request.getRequestDispatcher(config.Config.JSPMapper.PRIVILEGED_ADMIN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            doGet(request, response);
        }

        try {
            switch (action) {
                case "enableMaintenance":
                    service.ServerMaintenance.enableMaintenance("test");
                break;
                case "disableMaintenance":
                    service.ServerMaintenance.disableMaintenance();
                break;
                case "logStatistics":
                    logStatistics();
                break;
                case "calculateTFIDF":
                    calculateTFIDF();
                break;
                case "cleanup":
                    cleanup();
                break;
                case "sendNotification":
                    sendNotification(request, response);
                break;
            }
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to execute action {}, reason: {}", action, e.getMessage());

            request.setAttribute("error", e.getMessage());
        }
        

        doGet(request, response);
    }

    private static void logStatistics() throws java.sql.SQLException {
        dao.StatisticsDAO.SystemStatisticsManager.addStatistics();
    }

    private static void calculateTFIDF() throws java.sql.SQLException {
        dao.ProductDAO.TFIDF.computeTFIDF();
    }

    private static void cleanup() throws java.sql.SQLException, java.io.IOException {
        service.FileCleanupJob.cleanup();
    }

    private static void sendNotification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, java.sql.SQLException {
        model.Notification notification = new model.Notification();
        notification.setTitle(request.getParameter("title"));
        notification.setBody(request.getParameter("content"));
        notification.setIsRead(false);
        // excuse the uneasy and inconsistent usage of R-value string here
        notification.setUserId("all".equals(request.getParameter("target")) ? null : new model.User(Integer.parseInt(request.getParameter("userId"))));
        // service.Logging.logger.info("Adding notification title: {}, body: {}, target: {}", notification.getTitle(), notification.getBody(), notification.getUserId());
        dao.NotificationDAO.NotificationManager.add(notification);
    }
}
