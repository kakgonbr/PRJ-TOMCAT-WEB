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

        <form id="addProductForm" action="${pageContext.request.contextPath}/sellercenter/addproduct" method="post">
            <%-- TODO: ADD PRODUCT ID OF THE EDITING PRODUCT --%>
            <input type="hidden" name="id" value=""> 

            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="name" required><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea><br>
            <label>Category:</label>
            <div id="categoryFilter"></div> <%-- Danh sách danh mục hiển thị tại đây --%>
            <label onclick="onSelectCategory()">Continue</label>
            <ul id="variation-container">
            
            </ul>
            <table id="variation-table" border="1">
                <thead>
                    <tr>
                    <th>Product Item</th>
                    <th>Stock</th>
                    <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            <table>
            <label onclick="addProductItem()">Add product item</label>
            <button type="submit">Add</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>