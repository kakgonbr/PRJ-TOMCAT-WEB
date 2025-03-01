<%-- POSSIBLE REQUEST ATTRIBUTES: chatBoxes --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Chat">
    <jsp:attribute name="head">
        <t:resources />
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </jsp:attribute>

    <jsp:attribute name="header">
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Open Chats</h2>
        <c:forEach var="chat" items="${chatBoxes}">
            <c:if test="${chat.user1 == currentUser}">
                <button onclick="openChat('${chat.user2}')">Chat with ${chat.username2}</button>
            </c:if>
            <c:if test="${chat.user2 == currentUser}">
                <button onclick="openChat('${chat.user1}')">Chat with ${chat.username1}</button>
            </c:if>
        </c:forEach>

        <form action="${pageContext.request.contextPath}/chat" method="POST">
            <input type="text" name="targetUser"/>
            <button type="submit">Create chat</button>
        </form>


        <div id="chatPanel">
            <label>Chat</label><br>
            <div id="messages"
                style="border:1px solid #ccc; height:200px; overflow-y:scroll; margin:10px 0;">
            </div>
            <input type="text" id="message" placeholder="Type a message">
            <button onclick="sendMessage()">Send</button>
        </div>

        <script>
            var contextPath = "${pageContext.request.contextPath}";

            function openChat(targetUser) {
                if (window.chatSocket) {
                    window.chatSocket.close();
                }

                window.chatSocket = new WebSocket("wss://" + location.host + "/prj/chat");

                window.chatSocket.onopen = function () {
                    window.chatSocket.send(JSON.stringify({ targetUser: targetUser }));
                };

                console.log("Connected to chat with " + targetUser);

                window.chatSocket.onmessage = function (event) {
                    let chatPanel = document.getElementById("message");
                    chatPanel.innerHTML += "<p>" + event.data + "</p>";
                };

                window.chatSocket.onclose = function () {
                    console.log("Chat closed.");
                };

                fetch(contextPath + "/ajax/chat?targetUser=" + targetUser)
                    .then(response => response.json())
                    .then(messages => {
                        let chatPanel = document.getElementById("message");
                        chatPanel.innerHTML = "";
                        messages.forEach(msg => {
                            chatPanel.innerHTML += "<p>" + msg + "</p>";
                        });
                    });
            }

            function sendMessage() {
                let messageInput = document.getElementById("message");
                let message = messageInput.value.trim();
                messageInput.value = "";
                let messageObject = {
                    text: message
                };
                window.chatSocket.send(JSON.stringify(messageObject));

                if (message !== "" && window.chatSocket && window.chatSocket === WebSocket.OPEN) {
                    let messageObject = {
                        text: message
                    };
                    window.chatSocket.send(JSON.stringify(messageObject));
                }   
            }
        </script>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>