package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Mapped to /admin
 */
public class AdminServlet extends HttpServlet {
    /**
     * This methods expects the authentication has already been done by the filter
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(config.Config.JSPMapper.PRIVILEGED_ADMIN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            doGet(request, response);
        }

        switch (action) {
            case "enableMaintenance":
                service.ServerMaintenance.enableMaintenance("test");
            break;
            case "disableMaintenance":
                service.ServerMaintenance.disableMaintenance();
            break;
        }
    }
}
