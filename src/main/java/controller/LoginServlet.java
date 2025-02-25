package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ASUS
 */
public class LoginServlet extends HttpServlet {
    /**
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(config.Config.JSPMapper.LOGIN_JSP).forward(request, response);
    }

    /**
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userOrEmail = request.getParameter("userOrEmail");
        String password = request.getParameter("password");
        boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));
        model.User user;

        try {
            user = dao.UserDAO.UserFetcher.getUser(service.DatabaseConnection.getConnection(), userOrEmail, password);

            if (user == null) {
                throw new java.sql.SQLException("Failed to retreive user");
            }

            service.SessionAndCookieManager.createSession(user, request.getSession(), rememberMe);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Log in failed for request {}, tried: {} and {}", request.getRequestId(), userOrEmail, password);

            request.setAttribute("reason", "Invalid login");

            doGet(request, response);

            return;
        }

        // request.getRequestDispatcher(config.Config.JSPMapper.HOME_JSP).forward(request, response);

        // TODO: Add the path of the servlet that handles page redirection here
        if (!user.isAdmin()) {
            response.sendRedirect("");
            return;
        }

        // path of admin servlet here
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}

