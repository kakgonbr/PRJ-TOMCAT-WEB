<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Set Stock & Price">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
    </jsp:attribute>

    <jsp:attribute name="body">
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

        <form action="${pageContext.request.contextPath}/addproduct" method="post">
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