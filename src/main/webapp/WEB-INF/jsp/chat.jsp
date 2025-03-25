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
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container mt-4">
            <div class="row">
                <div class="col-md-4">
                    <h2 class="mb-3">Open Chats</h2>

                    <div class="list-group">
                        <c:forEach var="chat" items="${chatBoxes}">
                            <c:if test="${chat.user1 == currentUser}">
                                <button class="list-group-item list-group-item-action" onclick="openChat('${chat.user2}', '${chat.username2}')">
                                    Chat with ${chat.username2}
                                </button>
                            </c:if>
                            <c:if test="${chat.user2 == currentUser}">
                                <button class="list-group-item list-group-item-action" onclick="openChat('${chat.user1}', '${chat.username1}')">
                                    Chat with ${chat.username1}
                                </button>
                            </c:if>
                        </c:forEach>
                    </div>

                    <form action="${pageContext.request.contextPath}/chat" method="POST" class="mt-3">
                        <div class="input-group">
                            <input type="text" name="targetUser" class="form-control" placeholder="Enter username">
                            <button type="submit" class="btn btn-primary">Create Chat</button>
                        </div>
                    </form>
                </div>

                <div class="col-md-8">
                    <h2 class="mb-3">Chat</h2>
                    
                    <div id="chatPanel" class="card">
                        <div class="card-body">
                            <div id="messages" class="border rounded p-2" style="height: 200px; overflow-y: auto;">
                            </div>

                            <div class="input-group mt-2">
                                <input type="text" id="message" class="form-control" placeholder="Type a message">
                                <button class="btn btn-success" onclick="sendMessage()">Send</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>