<%-- POSSIBLE REQUEST ATTRIBUTES:  product (model.ProductDetailsWrapper) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Product">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="home"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <c:if test="${error == 'true'}">
            <h2>ERROR!!!!!!!!!!!!!!!!</h2>
        </c:if>
        <div id="product-info-container">
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>
