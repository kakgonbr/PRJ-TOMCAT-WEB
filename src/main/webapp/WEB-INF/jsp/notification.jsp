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
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var userId = "${sessionScope.user.id}";
            document.addEventListener("DOMContentLoaded", function () {
                fetchNotifications(userId);
            });
        </script>
        <style>
            .notification-item {
                padding: 10px;
                margin: 5px;
                border: 1px solid #ddd;
                cursor: pointer;
            }
            .notification-item.unread {
                background-color: #f8d7da;
            }
            .notification-item.read {
                background-color: #d4edda;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:error error="${error}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div id="notificationTable">
            <p>Loading notifications...</p>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>
