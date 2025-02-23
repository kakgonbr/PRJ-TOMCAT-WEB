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
            System.err.println("SQL exception occurred while connecting to the SQL Server.");
            e.printStackTrace();
            return;
        }

        System.out.println("Initializing database connection...");

        // JDBC URL for MS SQL Server
        String url = String.format("jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=true;", config.Config.DBConfig.DB_ADDRESS, config.Config.DBConfig.DB_NAME);
        String username = config.Config.DBConfig.DB_USERNAME;
        String password = config.Config.DBConfig.DB_PASSWORD;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established.");
            connection.setAutoCommit(config.Config.DBConfig.AUTO_COMMIT);

            // Save the connection in the ServletContext for use across the application
            sce.getServletContext().setAttribute("DB_CONNECTION", connection);
            service.DatabaseConnection.setConnection(connection);
        } catch (SQLException e) {
            System.out.println("Error establishing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Closing database connection...");

        // Close the database connection if it exists
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
