package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConnection {
    private static EntityManagerFactory factory;

    public static void initialize() {
        try {
            service.Logging.logger.info("Initializing Database Connection.");
            factory = Persistence.createEntityManagerFactory(config.Config.DBConfig.PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            service.Logging.logger.error("FAILED TO INTIALIZE DATABASE CONNECTION, REASON : {}", e.getMessage());
            
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
