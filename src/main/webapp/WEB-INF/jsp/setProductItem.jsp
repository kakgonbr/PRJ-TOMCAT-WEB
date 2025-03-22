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

        <div id="popup" class="popup">
            <h3>Edit Stock & Price</h3>
            <p>Product: <span id="productName"></span></p>
            <form action="${pageContext.request.contextPath}/addproduct" method="post">
                <input type="hidden" name="action" value="setStockAndPrice">
                <input type="hidden" id="productId" name="productItemId">
                <label>Stock:</label>
                <input type="number" id="stock" name="stock" min="0"><br>
                <label>Price:</label>
                <input type="number" id="price" name="price" min="0" step="0.01"><br>
                <button type="submit">Save</button>
                <button type="button" onclick="closePopup()">Cancel</button>
            </form>
        </div>
    </jsp:attribute>
</t:genericpage>