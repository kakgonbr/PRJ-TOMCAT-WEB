package listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class DatabaseConnectionListener implements ServletContextListener {

    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            service.Logging.logger.fatal("JDBC DRIVER CLASS NOT FOUND.");
            service.ServerLockDown.lockDownServer("JDBC DRIVER CLASS NOT FOUND");
            e.printStackTrace();
            return;
        }

        // System.out.println("Initializing database connection...");
        service.Logging.logger.info("Initializing database connection...");

        // JDBC URL for MS SQL Server
        String url = String.format("jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=true;", config.Config.DBConfig.DB_ADDRESS, config.Config.DBConfig.DB_NAME);
        String username = config.Config.DBConfig.DB_USERNAME;
        String password = config.Config.DBConfig.DB_PASSWORD;

        // For testing
        System.out.println(url);
        System.out.println(username);
        System.out.println(password);

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            // System.out.println("Database connection established.");
            connection.setAutoCommit(config.Config.DBConfig.AUTO_COMMIT);

            // Save the connection in the ServletContext for use across the application
            sce.getServletContext().setAttribute("DB_CONNECTION", connection);
            service.DatabaseConnection.setConnection(connection);

            service.Logging.logger.info("Database connection established.");
        } catch (SQLException e) {
            service.Logging.logger.fatal("FAILED TO CONNECT TO THE DATABASE. REASON: '{}'", e.getMessage());

            service.ServerLockDown.lockDownServer("FAILED TO CONNECT TO THE DATABASE");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        service.Logging.logger.info("Closing database connection...");
        
        // Close the database connection if it exists
        if (connection != null) {
            try {
                connection.close();
                service.Logging.logger.info("Database connection closed.");
            } catch (SQLException e) {
                service.Logging.logger.error("Failed to close database connection while destroying context");
                e.printStackTrace();
            }
        }
    }
}
