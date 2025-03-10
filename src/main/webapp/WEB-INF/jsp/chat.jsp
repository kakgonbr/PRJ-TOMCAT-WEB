<%-- POSSIBLE REQUEST ATTRIBUTES: chatBoxes --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Chat">
    <jsp:attribute name="head">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>
        
        <t:resources />
        <script src="${pageContext.request.contextPath}/resources/chat_js"></script>
    </jsp:attribute>

    <jsp:attribute name="header">
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Open Chats</h2>
        <c:forEach var="chat" items="${chatBoxes}">
            <c:if test="${chat.user1 == currentUser}">
                <button onclick="openChat('${chat.user2}', '${chat.username2}')">Chat with ${chat.username2}</button>
            </c:if>
            <c:if test="${chat.user2 == currentUser}">
                <button onclick="openChat('${chat.user1}', '${chat.username1}')">Chat with ${chat.username1}</button>
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

    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>