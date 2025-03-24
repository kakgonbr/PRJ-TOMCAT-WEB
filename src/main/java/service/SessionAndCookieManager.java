package service;

import model.User;

public final class SessionAndCookieManager {
    public static boolean createSessionFromCookie(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        jakarta.servlet.http.HttpSession session = null;

        if (cookies == null) return false;

        for (final jakarta.servlet.http.Cookie cookie : cookies) {
            if (cookie.getName().equals(config.Config.CookieMapper.REMEMBER_ME_COOKIE)) {
                try {
                    model.User user = dao.UserDAO.UserFetcher.getUser(cookie.getValue());
                    
                    session = request.getSession();
                    session.setAttribute("user", user);
                    
                    service.Logging.logger.info("Logged {} in with cookie {}", user.getUsername(), cookie);

                    return true;
                } catch (java.sql.SQLException e) {
                    // service.Logging.logger.warn("Failed to restore user session for session {}, reason: '{}'", session == null ? "none" : session.getId(), e.getMessage());
                } // try catch
            } // if
        } // for
        return false;
    } // public static boolean createSessionFromCookie

    /**
     *
     */
    public static String createCookie(User user, jakarta.servlet.http.HttpSession session) throws java.sql.SQLException {
        for (int i = 0; i < config.Config.CookieMapper.UUID_RETRY; ++i) {
            // user.setCookie(java.util.UUID.randomUUID().toString());
            String cookie = java.util.UUID.randomUUID().toString();
            try {
                if (!dao.UserDAO.UserFetcher.checkCookie(cookie)) {
                    throw new java.sql.SQLException("COOKIE ALREADY EXISTS: " + cookie);
                }

                dao.UserDAO.UserManager.updateCookie(user.getId(), cookie);
                
                return cookie;
            } catch (java.sql.SQLException e) {
                service.Logging.logger.warn("Failed to genereate UUID for session {}, retries remaining: {}, reason: {}", session.getId(), config.Config.CookieMapper.UUID_RETRY - i, e.getMessage());

                continue;
            }

        }
        throw new java.sql.SQLException("Could not insert a new cookie for session {}", session.getId());
    }
}
