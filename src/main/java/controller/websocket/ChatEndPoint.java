package controller.websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;

@ServerEndpoint(value = "/chat", configurator = controller.websocket.ChatConfigurator.class)
public class ChatEndPoint {
    private static Map<Integer, Session> activeSessions = new java.util.concurrent.ConcurrentHashMap<>(); 
    private HttpSession httpSession;
    private int boxId;
    private int targetId = -1;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        if (httpSession == null || httpSession.getAttribute("user") == null) {
            try {
                service.Logging.logger.info("{} tried to connect to an endpoint without authorization",
                        session.getId());

                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        service.Logging.logger.info("{} connected to an endpoint",
                ((model.User) httpSession.getAttribute("user")).getUsername());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        service.Logging.logger.info("Message received: {}, from websocket session {}", message, session.getId());

        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(message, type);
        model.User user = ((model.User) httpSession.getAttribute("user"));

        if (targetId == -1) {
            validateTarget(message, session, map, user);
            return;
        } // if (targetId == -1)

        Session targetSession = activeSessions.get(targetId);
        if (targetSession != null && targetSession.isOpen()) {
            try {
                targetSession.getBasicRemote().sendText(gson.toJson(message));
            } catch (IOException e) {
                service.Logging.logger.error("FAILED TO SEND MESSAGE TO SESSION {}", targetSession.getId());
            }
        }

        try {
            dao.ChatDAO.ContentManager.createContent(service.DatabaseConnection.getConnection(), boxId, user.getId(), map.get("text"));
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO INSERT CHAT CONTENT FOR {}, REASON: {}", session.getId(), e.getMessage());

            return;
        }

        service.Logging.logger.info("{} -? {}: {}", user.getId(), targetId, map.get("text"));
    }

    @OnClose
    public void onClose(Session session) {
        String username;
        model.User user = (model.User) httpSession.getAttribute("user");
        if (user != null) {
            activeSessions.remove(user.getId());
        }

        if (httpSession == null || (username = user.getUsername()) == null) {
            service.Logging.logger.info("{}'s endpoint disconnected.", session.getId());
            return;
        }

        service.Logging.logger.info("{} disconnected from an endpoint", username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username;
        if (httpSession == null || (username = (String) httpSession.getAttribute("user")) == null) {
            service.Logging.logger.info("{}'s endpoint encountered an error: {}", session.getId(),
                    throwable.getMessage());
            return;
        }

        service.Logging.logger.info("{}'s endpoint encountered an error: {}", username, throwable.getMessage());

        try {
            session.close(new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, "An error occurred."));
        } catch (IOException e1) {
            service.Logging.logger.warn("Failed to close the connection to {}, reason: {}", session.getId(),
                    e1.getMessage());
        }
    }

    private void validateTarget(String message, Session session, Map<String, String> map, model.User user) {
        int target = Integer.parseInt(map.get("targetUser"));

        try {
            boxId = dao.ChatDAO.BoxManager.getBox(service.DatabaseConnection.getConnection(), user.getId(), target).getId();

            targetId = target;
            activeSessions.put(user.getId(), session);

            service.Logging.logger.info("Session {} messaging userID {} is valid.", session.getId(), target);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Message from {} cannot be sent to user ID {}, reason: {} ,closing connection.",
                    session.getId(), target, e.getMessage());

            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
            } catch (IOException e1) {
                service.Logging.logger.warn("Failed to close the connection to {}, reason: {}", session.getId(),
                        e1.getMessage());
            }
            return;
        }
    }
}
