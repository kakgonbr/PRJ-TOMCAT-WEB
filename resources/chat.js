var senderUser;

function openChat(targetUser, sender) {
    senderUser = sender;

    if (window.chatSocket) {
        window.chatSocket.close();
    }

    window.chatSocket = new WebSocket("wss://" + location.host + "/prj/chat");

    window.chatSocket.onopen = function () {
        window.chatSocket.send(JSON.stringify({ targetUser: targetUser }));
    };

    console.log("Connected to chat with " + targetUser);

    window.chatSocket.onmessage = function (event) {
        let chatPanel = document.getElementById("messages");
        let msg = JSON.parse(event.data);

        chatPanel.innerHTML += "<p><strong>" + msg.time + " - " + senderUser + ":</strong> " + msg.message + "</p>";
    };

    window.chatSocket.onclose = function () {
        console.log("Chat closed.");
    };

    fetch(contextPath + "/ajax/chat?targetUser=" + targetUser)
        .then(response => response.json())
        .then(messages => {
            let chatPanel = document.getElementById("messages");
            chatPanel.innerHTML = "";

            messages.forEach(msg => {
                chatPanel.innerHTML += "<p><strong>" + msg.time + " - " + (msg.sender == targetUser ? senderUser : "You") + ":</strong> " + msg.message + "</p>";
            });
        })
        .catch(error => console.error("Error loading messages:", error));
}

function sendMessage() {
    let messageInput = document.getElementById("message");
    let message = messageInput.value.trim();
    messageInput.value = "";

    if (message !== "" && window.chatSocket && window.chatSocket.readyState === WebSocket.OPEN) {
        let messageObject = {
            text: message
        };
        window.chatSocket.send(JSON.stringify(messageObject));

        // ADD MESAGE TO PANEL HERE
        chatPanel.innerHTML += "<p><strong>Now - You:</strong> " + message + "</p>";
    }
}