package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Used for displaying user information
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(config.Config.JSPMapper.USER_DETAILS).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        model.User user = (model.User) request.getSession().getAttribute("user");

        if (action == null || action.isEmpty()) {
            doGet(request, response);
            return;
        }

        try {
            user = dao.UserDAO.UserFetcher.getUser(user.getUsername(), user.getPassword());

            if (user == null) {
                throw new java.sql.SQLException("Failed to retreive user");
            }

            request.getSession().setAttribute("user", user);

            switch (action) {
                case "displayName":
                    user.setDisplayName(request.getParameter("displayName"));
                    dao.UserDAO.UserManager.updateUser(user);
                    request.setAttribute("changed", "Display name");
                    break;
                    case "password":
                    String password = request.getParameter("password");
                    String confirm = request.getParameter("confirmPassword");
                    
                    if (!password.equals(confirm)) {
                        request.setAttribute("error", "Passwords do not match");
                        break;
                    }
                    
                    user.setPassword(password);
                    dao.UserDAO.UserManager.updateUser(user);
                    request.setAttribute("changed", "Password");
                break;
                case "credit":
                    // uh
                break;
            }
        } catch (java.sql.SQLException | NullPointerException e) {
            service.Logging.logger.warn("FAILED TO CHANGE USER {} INFORMATION, REASON: {}", user.getUsername(), e.getMessage());;

            request.setAttribute("error", "An error occurred, check your input");
        }

        doGet(request, response);
    }
}
