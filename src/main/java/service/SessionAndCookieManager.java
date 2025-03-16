package service;

import model.User;

public final class SessionAndCookieManager {
    public static boolean createSessionFromCookie(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        jakarta.servlet.http.HttpSession session = null;

        for (final jakarta.servlet.http.Cookie cookie : cookies) {
            if (cookie.getName().equals(config.Config.CookieMapper.REMEMBER_ME_COOKIE)) {
                try {
                    model.User user = dao.UserDAO.UserFetcher.getUser(cookie.getValue());

                    session = request.getSession();
                    session.setAttribute("user", user);

                    return true;
                } catch (java.sql.SQLException e) {
                    service.Logging.logger.warn("Failed to restore user session for session {}, reason: '{}'", session == null ? "none" : session.getId(), e.getMessage());
                } // try catch
            } // if
        } // for
        return false;
    } // public static boolean createSessionFromCookie

    /**
     *
     */
    public static void createSession(User user, jakarta.servlet.http.HttpSession session, boolean rememberMe) throws java.sql.SQLException {
        session.setAttribute("user", user);

        if (rememberMe) {
            for (int i = 0; i < config.Config.CookieMapper.UUID_RETRY; ++i) {
                // user.setCookie(java.util.UUID.randomUUID().toString());
                String cookie = java.util.UUID.randomUUID().toString();
                try {
                    if (!dao.UserDAO.UserFetcher.checkCookie(cookie)) {
                        throw new java.sql.SQLException("COOKIE ALREADY EXISTS: " + cookie);
                    }

                    dao.UserDAO.UserManager.updateCookie(user.getId(), cookie);
                    
                    return;
                } catch (java.sql.SQLException e) {
                    service.Logging.logger.warn("Failed to genereate UUID for session {}, retries remaining: {}, reason: {}", session.getId(), config.Config.CookieMapper.UUID_RETRY - i, e.getMessage());
                }
            }

            throw new java.sql.SQLException("Could not insert a new cookie for session {}", session.getId());
        }
    }
}
