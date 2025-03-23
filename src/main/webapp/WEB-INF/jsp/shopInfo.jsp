<%-- POSSIBLE REQUEST ATTRIBUTES:  shop (model.ShopWrapper) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Shop">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${param.shopId}"
            document.addEventListener("DOMContentLoaded", fetchCategory);
            document.addEventListener("DOMContentLoaded", fetchByShop);
            document.addEventListener("DOMContentLoaded", fetchShops);
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="home"/> --%>
        <t:userHeader user="${sessionScope.user.username}" />
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
        <p>Explore Shops</p>
        <div id="shopContainer">

        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>
