<%-- POSSIBLE REQUEST ATTRIBUTES:  shop (model.ShopWrapper) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="TEMPLATE">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shop = "${param.shopId}"
            document.addEventListener("DOMContentLoaded", fetchCategory);
            document.addEventListener("DOMContentLoaded", fetchByQueryAndCategory);
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="home"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <c:if test="${shop != null}">
            <ul>Shop:
                <li>ID: ${shop.id}</li>
                <li>Name: ${shop.name}</li>
                <li>Profile: <img src="${pageContext.request.contextPath}/resources/${shop.profileResource}" alt=""></li>
            </ul>
            <div id="categoryFilter"></div>
            <button onclick="fetchByShop()">Apply Filter</button>
            <p>Products:</p>
            <table border="1" id="productTable">
            </table>
        </c:if>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>
