package controller;

import java.io.IOException;
import java.util.Map;
import org.apache.hc.client5.http.ClientProtocolException;
import com.google.gson.JsonElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserRegistrationServlet extends HttpServlet {
    // can fill up really easily
    private static final java.util.Map<String, String> idToEmail = new java.util.HashMap<>();

    /**
     * Retrieves information from google or facebook, autofill what is available.
     * Then, displays the signup page as usual, with the information acquired from
     * google or facebook locked.<br>
     * </br>
     * Only autofill what is available from google or facebook.<br>
     * </br>
     * For normal signups, no information is available.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null && !code.isEmpty()) {
            String accessToken;
            String id = null;
            String email = null;
            Map<String, JsonElement> infoMap = null;
            try {
                accessToken = service.LoginService.getGGToken(code);
                infoMap = service.LoginService.getGGUserInfoJson(accessToken);
                id = infoMap.get("id").getAsString();
                email = infoMap.get("email").getAsString();
            } catch (ClientProtocolException e) {
                service.Logging.logger.error("ClientProtocolException error login service error, reason: {}", e.getMessage());
                
                response.sendRedirect(request.getContextPath() + "/login?error=access_denied");
                
                return;
            }
            
            
            service.Logging.logger.info("Received Login credentials: ID: {}, Email: {}", id, email);
            
            model.User user;
            try {
                user = dao.UserDAO.UserFetcher.getUserFromGoogle(id, email);
            } catch (java.sql.SQLException e) {
                service.Logging.logger.error("FAILED TO GET USER FROM DATABASE WITH GOOGLE CREDENTIALS, REASON: {}", e.getMessage());

                response.sendRedirect(request.getContextPath() + "/login?error=access_denied");

                return;
            }

            if (user != null) {
                request.getSession().setAttribute("user", user);

                request.getRequestDispatcher(request.getContextPath() + "/login"); // delegate to login servlet

                return;
            }

            
            idToEmail.put(id, email);
            
            request.setAttribute("email", email);
            request.setAttribute("id", id); // the user needs to send this back to the dopost method to complete
                                                 // registration, potential security risk
        }

        // send users to the signup screen for aditional information

        request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // need: email, username, phoneNumber, password
        String email;
        String googleId = request.getParameter("googleId");

        if (googleId != null && idToEmail.containsKey(googleId)) { // ensuring integrity, user cannot just edit their email to cause mismatch with the id
            email = idToEmail.get(googleId);
            idToEmail.remove(googleId);
        } else {
            email = request.getParameter("email");
        }

        String username = request.getParameter("username");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");

        // validations!!!!!!1
        
        if (username == null || !misc.Utils.Validator.username(username)) {
            request.setAttribute("error", "username");

            request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
            
            return;
        }

        if (email == null || !misc.Utils.Validator.email(email)) {
            request.setAttribute("error", "email");

            request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
            
            return;
        }

        if (password == null || !misc.Utils.Validator.password(password)) {
            request.setAttribute("error", "password");

            request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
            
            return;
        }

        if (phoneNumber == null || !misc.Utils.Validator.phoneNumber(phoneNumber)) {
            request.setAttribute("error", "phoneNumber");

            request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
            
            return;
        }

        try {
            model.User user = new model.User();

            user.setEmail(email);
            user.setUsername(username);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);
            user.setGoogleId(googleId); // In the database, this doesn't need to be unique, because for every id ther eis only 1 corresponding email.

            dao.UserDAO.UserManager.createUser(user);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Failed to sign up a user");

            request.setAttribute("error", "db");

            // Oh and make sure to autofill
            request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
            
            return;
        }

        // send the user back to login? Maybe? Ideally, this should already have all the information required to forward to user, but remember me option is missing

        response.sendRedirect(request.getContextPath() + "/login");
    }

}
