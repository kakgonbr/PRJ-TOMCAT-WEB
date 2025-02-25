package listeners;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionCountingListener implements HttpSessionListener {
    private static final AtomicInteger activeSessions = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = activeSessions.incrementAndGet();
        service.Logging.logger.info("Session created: {} | Active Sessions: {}", se.getSession().getId(), count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = activeSessions.decrementAndGet();
        service.Logging.logger.info("Session destroyed: {} | Active Sessions: {}", se.getSession().getId(), count);

        dao.StatisticsDAO.SystemStatisticsManager.SystemStatisticsContainer.reportSession(count);
    }
}
