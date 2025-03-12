package service;

import java.util.HashMap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConnection {
    private static EntityManagerFactory factory;

    static {
        
    }

    public static void initialize() {
        try {
            service.Logging.logger.info("Initializing Database Connection.");

            java.util.Map<String, String> properties = new HashMap<>();
            properties.put("jakarta.persistence.jdbc.url", String.format("jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=true;", System.getenv("DB_ADDRESS"), System.getenv("DB_NAME")));
            properties.put("jakarta.persistence.jdbc.user", System.getenv("DB_USERNAME"));
            properties.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

            factory = Persistence.createEntityManagerFactory(config.Config.DBConfig.PERSISTENCE_UNIT_NAME, properties);

            service.Logging.logger.info("Database connection established.");
        } catch (Exception e) {
            service.Logging.logger.error("FAILED TO INTIALIZE DATABASE CONNECTION, REASON : {}", e.getMessage());

            e.printStackTrace();
            
            service.ServerLockDown.lockDownServer("DATABASE CONNECTION INVALID");
        }
    }
    
    public static EntityManager getEntityManager() {
        if (factory == null || !factory.isOpen()) {
            service.Logging.logger.error("ENTITY MANAGER FACTORY IS INVALID");
            service.ServerLockDown.lockDownServer("DATABASE CONNECTION INVALID");
            
            return null;
        }
        
        return factory.createEntityManager();
    }
    
    public static void closeFactory() {
        service.Logging.logger.info("Closing Entity Manager Factory");
        if (factory != null && factory.isOpen()) {
            factory.close();
            service.Logging.logger.info("Entity Manager Factory Closed");
        } else {
            service.Logging.logger.warn("Entity Manager Factory is invalid, unable to close");
        }
    }
}
