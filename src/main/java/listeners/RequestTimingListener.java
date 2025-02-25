package listeners;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RequestTimingListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        service.Logging.logger.info("Request received: {}, from: {}", sre.getServletRequest().getRequestId(), sre.getServletRequest().getRemoteAddr());

        sre.getServletRequest().setAttribute("request_time_start", System.currentTimeMillis());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        service.Logging.logger.info("Request destroyed {}, from: {}", sre.getServletRequest().getRequestId(), sre.getServletRequest().getRemoteAddr());

        dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.reportResponseTime(System.currentTimeMillis() - (long) sre.getServletRequest().getAttribute("request_time_start"));
    }
}