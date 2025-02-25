package filters;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * All requests have to go through the maintenance filter, this filter however, allows logging in, but only allows the admin to be logged in.
 * The maintenance filter is not fit for cases where tblUser is unaccessible, as this will prevent the admin from accessing the admin panel.
 */
public class MaintenanceFilter implements jakarta.servlet.Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (service.ServerMaintenance.isActive()) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            if (httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).startsWith("/login")) {
                chain.doFilter(httpRequest, response);
                return;
            }

            HttpSession session = httpRequest.getSession(false);

            if (session != null && ((model.User) session.getAttribute("user")).isAdmin()) {
                chain.doFilter(httpRequest, response);
                return;
            }

            httpRequest.getRequestDispatcher(httpRequest.getContextPath() + "/redirect?page=" + controller.RedirectServlet.REDIRECT_MAINTENANCE).forward(httpRequest, response);;

            return;
        }

        chain.doFilter(request, response);
    } // public void doFilter

}
