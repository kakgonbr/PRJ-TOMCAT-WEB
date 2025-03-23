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
        request.getRequestDispatcher(config.Config.JSPMapper.PRIVILEGED_ADMIN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            doGet(request, response);
        }

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
        }

        doGet(request, response);
    }

    private static void logStatistics() {
        try {
            dao.StatisticsDAO.SystemStatisticsManager.addStatistics();
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Statistics Job failed, reason: {}", e.getMessage());
            
            return;
        }
    }

    private static void calculateTFIDF() {
        try {
            dao.ProductDAO.TFIDF.computeTFIDF();
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("TFIDF Job failed, reason: {}", e.getMessage());
            
            return;
        }
    }

    private static void cleanup() {
        try {
            service.FileCleanupJob.cleanup();
        } catch (IOException | java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO CLEAN UP FILES, REASON: {}", e.getMessage());
            
            return;
        }

        service.Logging.logger.info("Cleanup Job completed at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
    }
}
