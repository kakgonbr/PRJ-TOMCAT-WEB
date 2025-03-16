<%-- POSSIBLE REQUEST ATTRIBUTES:  email, id, error, taken --%>
<%-- POSSIBLE REQUEST PARAMETERS:  username, email, phoneNumber, password--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Sign Up">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
    <%-- TODO: Add informative error messages telling the user accepted formats --%>
        <c:if test="${error == 'db'}">
            <h1>A DATABASE ERROR OCCURRED!</h1>
        </c:if>
        <c:if test="${error == 'username'}">
            <h1>USERNAME!</h1>
        </c:if>
        <c:if test="${error == 'email'}">
            <h1>EMAIl!</h1>
        </c:if>
        <c:if test="${error == 'password'}">
            <h1>PASSWORD!</h1>
        </c:if>
        <c:if test="${error == 'phoneNumber'}">
            <h1>PHONENUMBER!</h1>
        </c:if>
        <c:if test="${taken == 'username'}">
            <h1>USERNAME IS TAKEN!</h1>
        </c:if>
        <c:if test="${taken == 'email'}">
            <h1>EMAIL IS TAKEN!</h1>
        </c:if>
        <c:if test="${taken == 'phoneNumber'}">
            <h1>PHONE NUMBER IS TAKEN!</h1>
        </c:if>
        <h1>Sign up</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <form action="${pageContext.request.contextPath}/signup" method="POST">
            <input type="hidden" name="googleId" value="${googleId}">
            <label>Username:</label>
            <input type="text" name="username" value="${param.username}">
            <label>Email:</label>
            <c:if test="${googleId == null || googleId == ''}">
                <input type="text" name="email" value="${param.email}">
            </c:if>
            <c:if test="${googleId != null && googleId != ''}">
                <input type="text" name="email" value="${email}" disabled>
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
