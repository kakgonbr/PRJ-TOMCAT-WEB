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

        // Allow public and resources
        if (path.startsWith("/login") || path.startsWith("/public") || path.startsWith("/home") || path.startsWith("resource")) {
            chain.doFilter(httpRequest, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        model.User user;

        if (session != null && (user = (model.User) session.getAttribute("user")) != null) {
            if (!user.isAdmin() && path.contains("/admin")) {
                service.Logging.logger.warn("Session {}, user ID {} attempted to go to page {}, request rejected.",
                        session.getId(), user.getId(), httpRequest.getRequestURI());
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (service.SessionAndCookieManager.createSessionFromCookie(session, httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/login?reason=invalid");
    } // public void doFilter

}
