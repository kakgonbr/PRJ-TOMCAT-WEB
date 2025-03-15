<%-- POSSIBLE REQUEST ATTRIBUTES:  email, id--%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="TEMPLATE">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <c:if test="${error == 'db'}">
            <h1>AN ERROR OCCURRED!</h1>
        </c:if>
        <h1>Sign up</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <form action="${pageContext.request.contextPath}/signup" method="POST">
            <input type="hidden" name="googleId" value="${googleId}">
            <label>Username:</label>
            <input type="text" name="email" value="${param.email}">
            <label>Email:</label>
            <c:if test="${googleId == null}">
                <input type="text" name="email" value="${param.email}">
            </c:if>
            <c:if test="${googleId != null}">
                <input type="text" name="email" value="${param.email}" disabled>
            </c:if>
            <label>Phone Number:</label>
            <input type="text" name="phoneNumber" value="${param.phoneNumber}">
            <label>Password:</label>
            <input type="password" name="password" value="${param.password}">
            <input type="submit" value="Sign up" />
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  /signup (POST) --%>
