<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        <script src="${pageContext.request.contextPath}/resources/addProduct_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${sessionScope.shopId}";

            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
            });
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="addProduct"/> --%>
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Add Product</h2>

        <%-- Hiển thị lỗi nếu có --%>
        <c:if test="${not empty error}">
            <div>${error}</div>
        </c:if>

        <form id="addProductForm" action="${pageContext.request.contextPath}/sellercenter/addproduct" method="post">
            <input type="hidden" name="action" value="addProduct">

            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="name" required><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea><br>
            <label>Category:</label>
            <div id="categoryFilter"></div> <%-- Danh sách danh mục hiển thị tại đây --%>
            <label onclick="onSelectCategory()">Continue</label>
            <ul id="variation-container">
            
            </ul>
            <table id="variation-table">
            
            <table>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>