package service;

/**
 * Should be run daily, puts today's statistics inside the database.
 */
public class StatisticsJob implements Runnable {

    @Override
    public void run() {
        service.Logging.logger.info("Statistics Job started at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
        
        try {
            dao.StatisticsDAO.SystemStatisticsManager.addStatistics(service.DatabaseConnection.getConnection());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Statistics Job failed, reason: {}", e.getMessage());
            
            return;
        }

        service.Logging.logger.info("Statistics Job completed at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
    }
}
