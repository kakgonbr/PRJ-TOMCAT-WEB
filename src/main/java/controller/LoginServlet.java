package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ASUS
 */
public class LoginServlet extends HttpServlet {
    /**
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        model.User user = null;
        if (session != null && (user = (model.User) session.getAttribute("user")) != null && action != null
                && action.equals("logout")) {
            try {
                session.invalidate();

                dao.UserDAO.UserManager.deleteCookie(user.getId());
            } catch (java.sql.SQLException e) {
                service.Logging.logger.info("Failed to delete cookie for user {}, reason: {}", user.getId(),
                        e.getMessage());
            }
        }

        session = request.getSession(false);

        if (session != null && user != null) {
            redirect(request, response, user.getIsAdmin());
            return;
        }

        request.getRequestDispatcher(config.Config.JSPMapper.LOGIN_JSP).forward(request, response);
    }

    /**
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userOrEmail = request.getParameter("userOrEmail");
        String password = request.getParameter("password");
        boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));
        model.User user;

        try {
            user = dao.UserDAO.UserFetcher.getUser(userOrEmail, password);

            if (user == null) {
                throw new java.sql.SQLException("Failed to retreive user");
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if (rememberMe) {
                String cookie = service.SessionAndCookieManager.createCookie(user, session);
                Cookie toBeAdded = new Cookie(config.Config.CookieMapper.REMEMBER_ME_COOKIE, cookie);
    
                toBeAdded.setSecure(true);
                toBeAdded.setHttpOnly(true);
                toBeAdded.setMaxAge(604800); // 1 week
    
                response.addCookie(toBeAdded);
            }
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Log in failed for request {}, tried: {} and {}. Reason: {}",
                    request.getRequestId(), userOrEmail, password, e.getMessage());

            request.setAttribute("reason", "invalid");

            doGet(request, response);

            return;
        }

        service.Logging.logger.info("User logged in: {}", user.getUsername());

        // request.getRequestDispatcher(config.Config.JSPMapper.HOME_JSP).forward(request,
        // response);

        redirect(request, response, user.getIsAdmin());
    }

    private static void redirect(HttpServletRequest request, HttpServletResponse response, boolean isAdmin)
            throws ServletException, IOException {
        if (!isAdmin) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // path of admin servlet here
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
