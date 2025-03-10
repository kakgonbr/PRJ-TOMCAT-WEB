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
 * Requests that go through privileged pages will be filtered here first.
 */
public class AuthenticationFilter implements jakarta.servlet.Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // service.Logging.logger.info("Filter AuthenticationFilter received a request going to: {}", path);

        // Allow non privileged list
        for (final String npPath : config.Config.nonPrivileged) {
            if (path.startsWith(npPath)) {
                chain.doFilter(httpRequest, response);
                return;
            }
        }

        HttpSession session = httpRequest.getSession(false);
        model.User user;

        if (session != null) { 
            if ((user = (model.User) session.getAttribute("user")) != null) {
                if (!user.getIsAdmin() && path.startsWith("/admin")) {
                    service.Logging.logger.warn("Session {}, user ID {} attempted to go to {}, request rejected.", session.getId(), user.getId(), httpRequest.getRequestURI());
    
                    ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/login?reason=forbidden");
                    return;
                }
                chain.doFilter(request, response);
                return;
            }

            if (service.SessionAndCookieManager.createSessionFromCookie(session, httpRequest)) {
                chain.doFilter(request, response);
                return;
            }
        }


        ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/login?reason=invalid");
    } // public void doFilter

}
