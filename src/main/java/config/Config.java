package config;

public class Config {
    public static class DBConfig {
        public static final String DB_ADDRESS = "prj-database.cbaqaq4aatpi.ap-southeast-1.rds.amazonaws.com";
        public static final String DB_USERNAME = "admin";
        public static final String DB_PASSWORD = "de190569";
        public static final String DB_NAME = "prj";
        public static final boolean AUTO_COMMIT = false;
        public static final int TIMEOUT = 30; // 30 seconds
        public static final int RETRY = 3;
    }
}
