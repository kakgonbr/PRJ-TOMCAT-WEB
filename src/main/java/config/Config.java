package config;

public final class Config {
    public static final String[] nonPrivileged = {"/login", "/public", "/home", "/redirect", "/resources", "/ipn", "/return"};
    public static final String[] nonMaintenance = {"/login", "/error", "/redirect"};

    public static final class DBConfig {
        // public static final String DB_ADDRESS = System.getenv("DB_ADDRESS");
        // public static final String DB_USERNAME = System.getenv("DB_USERNAME");
        // public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
        // public static final String DB_NAME = System.getenv("DB_NAME");
        // public static final boolean AUTO_COMMIT = false;
        // public static final int TIMEOUT = 30; // 30 seconds
        // public static final int RETRY = 3;

        public static final String PERSISTENCE_UNIT_NAME = "pu";
    }

    public static final class JSPMapper {
        public static final String LOGIN_JSP = "WEB-INF/jsp/login.jsp";
        public static final String HOME_JSP = "WEB-INF/jsp/home.jsp";
        public static final String MAINTENANCE_JSP = "WEB-INF/jsp/maintenance.jsp";
        public static final String ERROR_JSP = "WEB-INF/jsp/error.jsp";
        public static final String CHAT_JSP = "WEB-INF/jsp/chat.jsp";
        public static final String PAYMENT_RETURN_JSP = "WEB-INF/jsp/return.jsp";
        public static final String PAYMENT_PAY_JSP = "WEB-INF/jsp/pay.jsp";

        public static final String PRIVILEGED_ADMIN_JSP = "WEB-INF/jsp/admin/adminStats.jsp";
    }

    public static final class CookieMapper {
        public static final String REMEMBER_ME_COOKIE = "rememberMe";

        public static final int UUID_RETRY = 3;
    }

    public static final class Time {
        public static final java.time.format.DateTimeFormatter outputFormatDate = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        public static final java.time.format.DateTimeFormatter inputFormatDate = java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy");

        public static final java.time.format.DateTimeFormatter outputFormatTime = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");
        public static final java.time.format.DateTimeFormatter inputFormatTime = java.time.format.DateTimeFormatter.ofPattern("H:m:s - d/M/yyyy");
    }

    public static final class Resources {
        public static final String ROOT_DIR = "/prj/resources";
    }
}
