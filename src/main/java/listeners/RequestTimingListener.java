package listeners;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RequestTimingListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        service.Logging.logger.info("Request received.");

        sre.getServletRequest().setAttribute("start", System.currentTimeMillis());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        service.Logging.logger.info("Request destroyed.");

        dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.reportResponseTime(System.currentTimeMillis() - (long) sre.getServletRequest().getAttribute("start"));
    }
}