<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Set Stock & Price">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h3>Debugging Product Variations</h3>

        <h4>All Variation Values</h4>
        <ul>
            <c:forEach var="variationList" items="${requestScope.allVariationValues}">
                <li>
                    <strong>Variation:</strong>
                    <c:forEach var="variationValue" items="${variationList}" varStatus="loop">
                        ${variationValue.value}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                </li>
            </c:forEach>
        </ul>

        <h4>Generated Combinations</h4>
        <ul>
            <c:forEach var="combination" items="${requestScope.combinations}">
                <li>
                    <c:forEach var="variationValue" items="${combination}" varStatus="loop">
                        ${variationValue.value}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                </li>
            </c:forEach>
        </ul>

        <h4>Created Product Items</h4>
        <ul>
            <c:forEach var="item" items="${sessionScope.selectedProductItems}">
                <li>Product ID: ${item.productId.id}, Item ID: ${item.id}, Stock: ${item.stock}, Price: ${item.price}</li>
                </c:forEach>
        </ul>
        <h2>Set Stock & Price</h2>
        <!-- Hiển thị lỗi -->
        <c:if test="${not empty requestScope.errorMessages}">
            <div style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 10px;">
                <strong>Errors:</strong>
                <ul>
                    <c:forEach var="error" items="${requestScope.errorMessages}">
                        <li>${error}</li>
                        </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/sellercenter/addproduct" method="post">
            <input type="hidden" name="action" value="setStockAndPrice">
            <table border="1">
                <tr>
                    <th>ID</th>
                    <th>Stock</th>
                    <th>Price</th>
                </tr>
                <c:forEach var="item" items="${sessionScope.selectedProductItems}">
                    <tr>
                        <td>
                            <input type="hidden" name="productItemId" value="${item.id}">
                            ${item.id}
                        </td>
                        <td>
                            <input type="number" name="stock" value="${item.stock}">
                        </td>
                        <td>
                            <input type="text" name="price" value="${item.price}">
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <button type="submit">Save All</button>
        </form>
    </jsp:attribute>
</t:genericpage>