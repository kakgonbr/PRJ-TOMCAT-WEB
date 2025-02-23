package config;

public class Config {
    public static class DBConfig {
        public static final String DB_ADDRESS = System.getenv("DB_ADDRESS");
        public static final String DB_USERNAME = System.getenv("DB_USERNAME");
        public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
        public static final String DB_NAME = System.getenv("DB_NAME");
        public static final boolean AUTO_COMMIT = false;
        public static final int TIMEOUT = 30; // 30 seconds
        public static final int RETRY = 3;
    }
}
