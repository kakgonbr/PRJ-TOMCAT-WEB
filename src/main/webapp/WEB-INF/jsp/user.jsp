<%-- POSSIBLE REQUEST ATTRIBUTES: --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="User">
    <jsp:attribute name="head">
        <t:resources />
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:error error="${error}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <%-- <c:if test="${error != null && error !=''}">
            <p>Error: ${error}.</p>
        </c:if> --%>
        <c:if test="${changed != null && changed != ''}">
            <p>Changed ${changed}.</p>
        </c:if>
        <p>Profile: </p>
        <img src="${pageContext.request.contextPath}/resources/${sessionScope.user.profileStringResourceId.id}">
        <form action="${pageContext.request.contextPath}/user" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="profilePic">
            <input type="file" name="image">
            <input type="submit" value="Update" />
        </form>
        <label>Username:</label>
        <input type="text" name="username" value="${sessionScope.user.username}" disabled>
        <form action="${pageContext.request.contextPath}/user" method="POST">
            <input type="hidden" name="action" value="displayName">
            <label>Display Name:</label>
            <input type="text" name="displayName" value="${sessionScope.user.displayName}">
            <input type="submit" value="Update" />
        </form>
        <label>Phone Number:</label>
        <input type="text" name="phoneNumber" value="${sessionScope.user.phoneNumber}" disabled>
        <form action="${pageContext.request.contextPath}/user" method="POST">
            <input type="hidden" name="action" value="password">
            <label>Change password:</label>
            <input type="password" name="password">
            <label>Confirm Password:</label>
            <input type="password" name="confirmPassword">
            <input type="submit" value="Change" />
        </form>
        <form action="${pageContext.request.contextPath}/user" method="POST">
            <input type="hidden" name="action" value="credit">
            <label>Credit:</label>
            <input type="text" name="credit" value="${sessionScope.user.credit}">
            <input type="submit" value="Add Credit" />
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS: /user --%>