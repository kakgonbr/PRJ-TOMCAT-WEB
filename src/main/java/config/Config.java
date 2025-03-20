package config;

public final class Config {
    public static final String[] nonPrivileged = {"/login", "/public", "/home", "/redirect", "/resources", "/ipn", "/return", "/logs", "/ajax", "/signup", "/product", "/shop", "/category"};
    public static final String[] nonMaintenance = {"/login", "/error", "/redirect"};
    
    public static final String LOG_LOCATION = "/prj/logs/app.log";

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
        public static final String LOGIN_JSP = "/WEB-INF/jsp/login.jsp";
        public static final String HOME_JSP = "/WEB-INF/jsp/home.jsp";
        public static final String MAINTENANCE_JSP = "/WEB-INF/jsp/maintenance.jsp";
        public static final String ERROR_JSP = "/WEB-INF/jsp/error.jsp";
        public static final String CHAT_JSP = "/WEB-INF/jsp/chat.jsp";
        public static final String PAYMENT_RETURN_JSP = "/WEB-INF/jsp/return.jsp";
        public static final String PAYMENT_PAY_JSP = "/WEB-INF/jsp/pay.jsp";
        public static final String LOG_JSP = "/WEB-INF/jsp/log.jsp";
        public static final String SIGNUP_JSP = "/WEB-INF/jsp/signup.jsp";
        public static final String ADMIN_CONTROL_PANEL = "/WEB-INF/jsp/admin/adminControlPanel.jsp";
        public static final String ADMIN_EDIT_RESOURCE = "/WEB-INF/jsp/admin/resourceEdit.jsp";
        public static final String ADMIN_EDIT_PRODUCT = "/WEB-INF/jsp/admin/productEdit.jsp";
        public static final String ADMIN_EDIT_USER = "/WEB-INF/jsp/admin/userEdit.jsp";
        public static final String ADMIN_EDIT_SHOP = "/WEB-INF/jsp/admin/shopEdit.jsp";
        public static final String ADMIN_EDIT_PROMOTION = "/WEB-INF/jsp/admin/promotionEdit.jsp";
        public static final String PRODUCT_DETAILS = "/WEB-INF/jsp/productInfo.jsp";
        public static final String CATEGORY_DETAILS = "/WEB-INF/jsp/categoryInfo.jsp";
        public static final String SHOP_DETAILS = "/WEB-INF/jsp/shopInfo.jsp";
        public static final String SHOP_SIGNUP = "/WEB-INF/jsp/shopSignup.jsp";
        public static final String CHECKOUT = "/WEB-INF/jsp/checkout.jsp";
        public static final String SELLER_CENTER = "/WEB-INF/jsp/sellercenter.jsp";
        public static final String USER_DETAILS = "/WEB-INF/jsp/user.jsp";
        public static final String ADD_PRODUCT = "/WEB-INF/jsp/addProduct.jsp";
        public static final String CART_JSP = "/WEB-INF/jsp/cart.jsp";

        public static final String PRIVILEGED_ADMIN_JSP = "/WEB-INF/jsp/admin/adminStats.jsp";
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

    public static final class MailConfig {
        public static final String EMAIL_AUTH_USER = System.getenv("EMAIL_AUTH_USER");
        public static final String EMAIL_AUTH_PASSWORD = System.getenv("EMAIL_AUTH_PASSWORD"); // Dangerous
        public static final String EMAIL_FROM = EMAIL_AUTH_USER;
    }

    public static class GGLoginConfig {
        public static final String GOOGLE_CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
        public static final String GOOGLE_CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
        public static final String GOOGLE_REDIRECT_URI = "https://kakgonbri.zapto.org:8443/prj/signup";
        public static final String GOOGLE_GRANT_TYPE = "authorization_code";
        public static final String GOOGLE_LINK_GET_TOKEN = "https://oauth2.googleapis.com/token";
        public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    }

    public static class FBLoginConfig {
        public static final String FACEBOOK_CLIENT_ID = System.getenv("FACEBOOK_CLIENT_ID");
        public static final String FACEBOOK_CLIENT_SECRET = System.getenv("FACEBOOK_CLIENT_SECRET");
        public static final String FACEBOOK_REDIRECT_URI = "https://kakgonbri.zapto.org/prj/signup?method=fb";
        public static final String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/v22.0/oauth/access_token";
        public static final String FACEBOOK_LINK_GET_USER_INFO = "https://graph.facebook.com/me?fields=id,name,email&access_token=";
    }

    public static class GoongMapAPIConfig {
        public static final String API_KEY = System.getenv("GOONG_API_KEY");
        public static final String API_AUTOCOMPLETE_LINK = "https://rsapi.goong.io/place/autocomplete?";
        public static final String API_GEO_CODE = "https://rsapi.goong.io/geocode?";
        public static final String API_GEO_DIRECTION = "https://rsapi.goong.io/direction?";
    }
}
