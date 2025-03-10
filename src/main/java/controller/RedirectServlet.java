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
public class RedirectServlet extends HttpServlet {
    public static final String REDIRECT_MAINTENANCE = "maintenance"; 
    public static final String REDIRECT_LOG = "log"; 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("page");

        if (target != null) {
            switch (target) {
                case REDIRECT_MAINTENANCE:
                    request.getRequestDispatcher(config.Config.JSPMapper.MAINTENANCE_JSP).forward(request, response);
                return;
                case REDIRECT_LOG:
                    request.getRequestDispatcher(config.Config.JSPMapper.LOG_JSP).forward(request, response);
                return;
            }
        }

        request.setAttribute("code", 404);
        request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
