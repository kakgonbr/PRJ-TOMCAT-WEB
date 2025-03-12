package controller;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonElement;

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
        jakarta.servlet.http.HttpSession session = request.getSession(false);

        if (session != null) {
            model.User user = (model.User) session.getAttribute("user");
            if (user != null) {
                redirect(request, response, user.getIsAdmin());
                return;
            }
        }

        //check if login by google or fb
        //get the code token
        String code = request.getParameter("code");
        if(code==null || code.isEmpty())
        {
            request.getRequestDispatcher(config.Config.JSPMapper.LOGIN_JSP).forward(request, response);
            return;
        }
        else 
        {
            String accessToken;
            String id;
            String email; 
            Map<String, JsonElement> infoMap= null;
            service.Logging.logger.error(code);
            try {
                switch (request.getParameter("method")) {
                    case "gg":
                        accessToken= service.LoginService.getGGToken(code);
                        infoMap = service.LoginService.getGGUserInfoJson(accessToken);
                        id= infoMap.get("id").getAsString();
                        email= infoMap.get("email").getAsString();
                        break;
                    case "fb":
                        accessToken= service.LoginService.getFBToken(code);
                        infoMap = service.LoginService.getFBUserInfoJson(accessToken);
                        id= infoMap.get("id").getAsString();
                        //this email can be null
                        email= infoMap.get("email").getAsString();
                        break;
                    default:
                        break;
                    //dispatch to home.jsp
                }
                response.sendRedirect(config.Config.JSPMapper.HOME_JSP);
                return;    
            } catch (ClientProtocolException e) {
                service.Logging.logger.error("ClientProtocolException error login service error");
            }
            
        }
        
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
            user = dao.UserDAO.UserFetcher.getUser(userOrEmail, password);

            if (user == null) {
                throw new java.sql.SQLException("Failed to retreive user");
            }

            service.SessionAndCookieManager.createSession(user, request.getSession(), rememberMe);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Log in failed for request {}, tried: {} and {}. Reason: {}", request.getRequestId(), userOrEmail, password, e.getMessage());

            request.setAttribute("reason", "invalid");

            doGet(request, response);

            return;
        }

        service.Logging.logger.info("User logged in: {}", user.getUsername());

        // request.getRequestDispatcher(config.Config.JSPMapper.HOME_JSP).forward(request, response);

        redirect(request, response, user.getIsAdmin());
    }

    private static void redirect(HttpServletRequest request, HttpServletResponse response, boolean isAdmin) throws ServletException, IOException {
        if (!isAdmin) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // path of admin servlet here
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}

