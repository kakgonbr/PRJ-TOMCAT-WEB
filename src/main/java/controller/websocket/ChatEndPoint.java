package controller.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat", configurator = controller.websocket.ChatConfigurator.class)
public class ChatEndPoint {
    private HttpSession httpSession;
    private int targetId = -1;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        if (httpSession == null || httpSession.getAttribute("username") == null) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        service.Logging.logger.info("{} connected to an endpoint", (String) httpSession.getAttribute("username"));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        service.Logging.logger.info("Message received: {}, from websocket session {}", message, session.getId());
        
    }

    @OnClose
    public void onClose(Session session) {
        if (httpSession == null) return;
        String username;
        if ((username = (String) httpSession.getAttribute("username")) == null) return;

        service.Logging.logger.info("{} disconnected from an endpoint", username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        if (httpSession == null) return;
        String username;
        if ((username = (String) httpSession.getAttribute("username")) == null) return;

        service.Logging.logger.info("{}'s endpoint encountered an error: {}", username, throwable.getMessage());
    }
}
