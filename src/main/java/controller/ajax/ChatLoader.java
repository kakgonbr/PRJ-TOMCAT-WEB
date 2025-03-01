package controller.ajax;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ASUS
 */
public class ChatLoader extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.User user = (model.User) session.getAttribute("user");
        String targetUsername = request.getParameter("targetUser");

        try {
            int targetUserId = dao.UserDAO.UserFetcher.getUserFromUsername(service.DatabaseConnection.getConnection(), targetUsername).getId();

            int box = dao.ChatDAO.BoxManager.getBox(service.DatabaseConnection.getConnection(), user.getId(), targetUserId).getId();

            java.util.List<model.ChatContent> messages = dao.ChatDAO.ContentManager.getContent(service.DatabaseConnection.getConnection(), box);

            response.setContentType("application/json");

            String json = new com.google.gson.Gson().toJson(messages);

            service.Logging.logger.info("Sending back to user {} json {}", user.getId(), json);

            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET CHAT CONTENT FOR {}, REASON: {}", user.getId(), e.getMessage());

            request.setAttribute("code", 500);
            request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
            return;
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
