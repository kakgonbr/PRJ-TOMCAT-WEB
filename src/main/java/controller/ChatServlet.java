package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.User user = (model.User) request.getSession(false).getAttribute("user");

        try {
            java.util.List<model.ChatBox> chatBoxes = dao.ChatDAO.BoxManager.getBoxes(service.DatabaseConnection.getConnection(), user.getId());

            request.setAttribute("chatBoxes", chatBoxes);
            request.setAttribute("currentUser", user.getId());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET CHATBOXES FOR {}, REASON: {}", user.getId(), e.getMessage());

            request.setAttribute("code", 500);
            request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
            return;
        }

        request.getRequestDispatcher(config.Config.JSPMapper.CHAT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        createChat(request, response);
    }

    private void createChat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.User user = (model.User) request.getSession(false).getAttribute("user");
        String toName = request.getParameter("targetUser");

        try {
            model.User toUser = dao.UserDAO.UserFetcher.getUserFromUsername(service.DatabaseConnection.getConnection(), toName);

            dao.ChatDAO.BoxManager.createBox(service.DatabaseConnection.getConnection(), user.getId(), toUser.getId());
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO CREATE CHATBOX FOR {}, REASON: {}", user.getId(), e.getMessage());

            request.setAttribute("code", 500);
            request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
            return;
        }

        doGet(request, response);
    }
}
