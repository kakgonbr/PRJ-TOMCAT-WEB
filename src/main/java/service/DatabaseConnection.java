package service;

public final class DatabaseConnection {
    private static java.sql.Connection connection;

    public static void setConnection(java.sql.Connection t_connection) {
        connection = t_connection;
    }

    public static java.sql.Connection getConnection() throws java.sql.SQLException {
        for (int i = 0; i < config.Config.DBConfig.RETRY; ++i) {
            if (connection.isValid(config.Config.DBConfig.TIMEOUT)) {
                return connection;
            }
        }

        service.ServerLockDown.lockDownServer("DATABASE CONNECTION CLOSED");
        
        throw new java.sql.SQLException("DATABASE CONNECTION CLOSED");
    }
}
