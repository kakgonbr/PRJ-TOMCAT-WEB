<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Private Chat</title>
</head>
<body>
    <h2>Private Chat</h2>
    <label>Username: <input type="text" id="username"></label>
    <button onclick="connect()">Connect</button>
    
    <div id="chat-container" style="display:none;">
        <label>Chat with: <input type="text" id="recipient"></label><br>
        <div id="messages" style="border:1px solid #ccc; height:200px; overflow-y:scroll; margin:10px 0;"></div>
        <input type="text" id="message" placeholder="Type a message">
        <button onclick="sendMessage()">Send</button>
    </div>

    <script>
        let ws;
        function connect() {
            const username = document.getElementById('username').value.trim();
            if (!username) {
                alert("Please enter a username!");
                return;
            }

            ws = new WebSocket('ws://' + location.host + '/your-app-context/chat/' + username);

            ws.onopen = () => {
                document.getElementById('chat-container').style.display = 'block';
                console.log("Connected as " + username);
                ws.send(JSON.stringify({ type: "auth", username: username }));
            };

            ws.onmessage = (event) => {
                const messageDiv = document.createElement('div');
                messageDiv.textContent = event.data;
                document.getElementById('messages').appendChild(messageDiv);
            };

            ws.onclose = () => {
                console.log("Disconnected");
            };

            ws.onerror = (err) => {
                console.error("WebSocket Error:", err);
            };
        }

        function sendMessage() {
            const recipient = document.getElementById('recipient').value.trim();
            const message = document.getElementById('message').value.trim();
            if (recipient && message) {
                ws.send(recipient + ":" + message);
                document.getElementById('message').value = "";
            }
        }
    </script>
</body>
</html>
