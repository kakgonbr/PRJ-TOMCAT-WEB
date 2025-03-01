package controller.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;

@ServerEndpoint(value = "/chat", configurator = controller.websocket.ChatConfigurator.class)
public class ChatEndPoint {
    private HttpSession httpSession;
    private int targetId = -1;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        if (httpSession == null || httpSession.getAttribute("user") == null) {
            try {
                service.Logging.logger.info("{} tried to connect to an endpoint without authorization", session.getId());

                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        service.Logging.logger.info("{} connected to an endpoint", ((model.User) httpSession.getAttribute("user")).getUsername());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        service.Logging.logger.info("Message received: {}, from websocket session {}", message, session.getId());
        
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = (new Gson()).fromJson(message, type);
        
        if (targetId == -1) {
            int target =Integer.parseInt(map.get("targetUser"));

            try {
                dao.ChatDAO.BoxManager.getBox(service.DatabaseConnection.getConnection(), ((model.User) httpSession.getAttribute("user")).getId(), target);
                
                targetId = target;
            } catch (java.sql.SQLException e) {
                service.Logging.logger.warn("Message from {} cannot be sent to user ID {}, reason: {} ,closing connection.", session.getId(), target, e.getMessage());
                
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
                } catch (IOException e1) {
                    service.Logging.logger.warn("Failed to close the connection to {}, reason: {}", session.getId(), e1.getMessage());
                }
                return;
            }

            return;
        } // if (targetId == -1)
        
        service.Logging.logger.info("Message to be delivered: {}, from websocket session {}", message, session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        if (httpSession == null) return;
        String username;
        if ((username = (String) httpSession.getAttribute("username")) == null) {
            service.Logging.logger.info("{}'s endpoint disconnected.", session.getId());
            return;
        }

        service.Logging.logger.info("{} disconnected from an endpoint", username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        if (httpSession == null) return;
        String username;
        if ((username = (String) httpSession.getAttribute("username")) == null)  {
            service.Logging.logger.info("{}'s endpoint encountered an error: {}", session.getId(), throwable.getMessage());
            return;
        }
        
        service.Logging.logger.info("{}'s endpoint encountered an error: {}", username, throwable.getMessage());
    }
}
