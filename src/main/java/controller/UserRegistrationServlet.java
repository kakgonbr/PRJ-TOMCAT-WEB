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
                service.Logging.logger.error("ClientProtocolException error login service error");
            }
            service.Logging.logger.info("Received Login credentials: ID: {}, Email: {}", id, email);

            request.setAttribute("email", email);
            request.setAttribute("id", id); // the user needs to send this back to the dopost method to complete
                                            // registration, potential security risk
            request.setAttribute("text", "hello");
        }

        // send users to the signup screen for aditional information

        request.getRequestDispatcher(config.Config.JSPMapper.SIGNUP_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }

}
