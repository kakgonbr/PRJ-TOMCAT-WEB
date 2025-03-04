package service;

/**
 * Should be run daily, computes TFIDF.
 */
public class TFIDFJob implements Runnable {

    @Override
    public void run() {
        service.Logging.logger.info("TFIDF Job started at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
        
        try {
            dao.ProductDAO.TFIDF.computeTFIDF(service.DatabaseConnection.getConnection());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("TFIDF Job failed, reason: {}", e.getMessage());
            
            return;
        }

        service.Logging.logger.info("TFIDF Job completed at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
    }
}
