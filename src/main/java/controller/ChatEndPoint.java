package controller;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatEndPoint {
    @OnOpen
    public void onOpen(Session session) {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        service.Logging.logger.info("Message received: {}, from session {}", message, session.getId());
        // if (!userSessions.containsKey(session.getId())) {
        //     // First message should be the username
        //     try {
        //         String username = extractUsernameFromMessage(message);
        //         if (username != null) {
        //             userSessions.put(username, session);
        //             System.out.println(username + " connected.");
        //             return;
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }

        // // Regular message processing
        // String sender = getUsernameBySession(session);
        // if (sender == null)
        //     return;

        // // Expected message format: "recipient:message_text"
        // String[] parts = message.split(":", 2);
        // if (parts.length < 2)
        //     return;

        // String recipient = parts[0].trim();
        // String messageText = parts[1].trim();

        // Session recipientSession = userSessions.get(recipient);
        // if (recipientSession != null && recipientSession.isOpen()) {
        //     try {
        //         recipientSession.getBasicRemote().sendText(sender + ": " + messageText);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    @OnClose
    public void onClose(Session session) {
    
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }
}
