<%-- 
    Document   : sellercenter
    Created on : Mar 19, 2025, 4:28:21 PM
    Author     : THTN0
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<t:genericpage title="ShopSignup">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h1>hello seller center</h1>

    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
        <h2><a href="${pageContext.request.contextPath}/redirect?page=log">View Log</a></h2>
    </jsp:attribute>
</t:genericpage>
