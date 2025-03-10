<%-- POSSIBLE REQUEST ATTRIBUTES: reason(invalid, forbidden) --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<t:genericpage title="Login">
    <jsp:attribute name="head">
        <t:resources />
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>LOGIN NOW</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <form action="${pageContext.request.contextPath}/login" method="POST">
            <input type="text" name="userOrEmail">
            <input type="text" name="password">
            <input type="submit" value="Login">
        </form>
        <br></br>
        <h2><a href="${pageContext.request.contextPath}/redirect?page=log">View Log</a></h2>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS: /login (POST) --%>