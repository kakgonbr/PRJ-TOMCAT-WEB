package listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class SchedulerListener implements ServletContextListener {
    private static ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new service.StatisticsJob(), 24 - java.time.LocalDateTime.now().getHour(), 24, TimeUnit.HOURS);
        scheduler.scheduleAtFixedRate(new service.TFIDFJob(), 24 - java.time.LocalDateTime.now().getHour(), 24, TimeUnit.HOURS);
        scheduler.scheduleAtFixedRate(new service.FileCleanupJob(), 24 - java.time.LocalDateTime.now().getHour(), 24, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}