package controller;

import java.io.IOException;
import java.util.Map;
import org.apache.hc.client5.http.ClientProtocolException;
import com.google.gson.JsonElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GgFbLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code= request.getParameter("code");
        if(code==null || code.isEmpty())
        {
            request.getRequestDispatcher(config.Config.JSPMapper.LOGIN_JSP).forward(request, response);
            return;
        }
        else 
        {
            String accessToken;
            String id=null;
            String email = null; 
            Map<String, JsonElement> infoMap= null;
            try {
                switch (request.getParameter("method")) {
                    case "gg":
                        accessToken= service.LoginService.getGoogleToken(code);
                        infoMap = service.LoginService.getGGUserInfoJson(accessToken);
                        id= infoMap.get("id").getAsString();
                        email= infoMap.get("email").getAsString();
                        break;
                    case "fb":
                    /* 
                        accessToken= Login.getFBToken(code);
                        map = Login.getFBUserInfoJson(accessToken);
                        json= map.get("id").getAsString();
                        json= json + ", " + map.get("email").getAsString();
                        break;
                        */
                    default:
                        break;
                }
                response.sendRedirect(config.Config.JSPMapper.HOME_JSP);    
            } catch (ClientProtocolException e) {
                service.Logging.logger.error("ClientProtocolException error login service error");
            }
        service.Logging.logger.info("Received Login credentials: ID: {}, Email: {}", id, email);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(req, resp);
    }
    
}
