<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Set Stock & Price">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script>
            function openPopup(productId, name, stock, price) {
                document.getElementById("popup").style.display = "block";
                document.getElementById("productId").value = productId;
                document.getElementById("productName").textContent = name;
                document.getElementById("stock").value = stock || 0;
                document.getElementById("price").value = price || 0;
            }

            function closePopup() {
                document.getElementById("popup").style.display = "none";
            }
        </script>
        <style>
            .popup {
                display: none;
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background: white;
                padding: 20px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Set Stock & Price</h2>

        <form action="${pageContext.request.contextPath}/addproduct" method="post">
            <input type="hidden" name="action" value="setStockAndPrice">
            <table border="1">
                <tr>
                    <th>Name</th>
                    <th>Stock</th>
                    <th>Price</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="item" items="${sessionScope.selectedProducts}">
                    <tr>
                        <td>${item.name}</td>
                        <td><input type="number" name="stock" min="0" value="${item.stock}"></td>
                        <td><input type="number" name="price" min="0" step="0.01" value="${item.price}"></td>
                        <td>
                            <input type="hidden" name="productItemId" value="${item.id}">
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