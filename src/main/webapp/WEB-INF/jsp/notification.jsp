<%-- 
    Document   : notification
    Created on : Mar 26, 2025, 1:07:52 AM
    Author     : hoahtm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="User">
    <jsp:attribute name="head">
        <t:resources />
        <style>
            .notification-item {
                padding: 10px;
                margin: 5px;
                border: 1px solid #ddd;
                cursor: pointer;
            }
            .notification-item.unread {
                background-color: #f8d7da; /* Màu đỏ nhạt cho chưa đọc */
            }
            .notification-item.read {
                background-color: #d4edda; /* Màu xanh nhạt cho đã đọc */
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:error error="${param.error}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2 class="text-dark">Notification</h2>

        <form action="${pageContext.request.contextPath}/notification" method="POST">
            <c:forEach var="noti" items="${notification}">
                <div class="notification-item ${noti.isRead ? 'read' : 'unread'}">
                    <input type="checkbox" name="notificationIds" value="${noti.id}" />
                    <h3>${noti.title}</h3>
                    <p>${noti.body}</p>
                </div>
            </c:forEach>
            <br>
            <button type="submit">Đánh dấu đã đọc</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>
