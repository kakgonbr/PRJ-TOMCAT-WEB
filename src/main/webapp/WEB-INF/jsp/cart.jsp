<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Cart">
    <jsp:attribute name="head">
        <t:resources/>
        <style>
            .cart-container {
                max-width: 1000px;
                margin: auto;
                padding: 20px;
                background: #1e1e1e;
                border-radius: 10px;
                color: white;
            }
            .cart-table {
                width: 100%;
                border-collapse: collapse;
                text-align: left;
            }
            .cart-table th, .cart-table td {
                padding: 10px;
                border-bottom: 1px solid #444;
            }
            .cart-table th {
                background: #2a2a2a;
            }
            .cart-table img {
                width: 60px;
                height: 60px;
                object-fit: cover;
                border-radius: 5px;
            }
            .cart-actions button {
                background: #3b82f6;
                color: white;
                padding: 5px 10px;
                border-radius: 5px;
                border: none;
                cursor: pointer;
                transition: 0.3s;
            }
            .cart-actions button:hover {
                background: #2563eb;
            }
            .checkout-btn {
                display: block;
                margin-top: 15px;
                text-align: center;
                padding: 10px;
                background: #10b981;
                color: white;
                border-radius: 5px;
                text-decoration: none;
                font-weight: bold;
                transition: 0.3s;
            }
            .checkout-btn:hover {
                background: #059669;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="cart-container">
            <h2 class="text-xl font-bold mb-4">🛒 Your Shopping Cart</h2>
            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Image</th>
                        <th>Product</th>
                        <th>Shop</th>
                        <th>Promotion</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Customization</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cartItem" items="${cartItems}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/resources/${cartItem.productWrapper.thumbnail}" alt="Product Image">
                            </td>
                            <td>${cartItem.productWrapper.name}</td>
                            <td>${cartItem.productWrapper.shop.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${cartItem.productWrapper.promotion.type}">
                                        - ${cartItem.productWrapper.promotion.value} VND
                                    </c:when>
                                    <c:otherwise>
                                        - ${cartItem.productWrapper.promotion.value} %
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="POST">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="productItemId" value="${cartItem.id}">
                                    <input type="number" name="quantity" min="1" value="${cartItem.quantity}" class="border rounded px-2 py-1 w-16">
                                    <button type="submit" class="cart-actions">Update</button>
                                </form>
                            </td>
                            <td>${cartItem.productItem.price}</td>
                            <td>${cartItem.productItem.stock}</td>
                            <td>
                                <c:forEach var="customization" items="${cartItem.productItem.customizations}">
                                    <p>${customization.name}: ${customization.value} ${customization.unit}</p>
                                </c:forEach>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="POST">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productItemId" value="${cartItem.id}">
                                    <button type="submit" class="cart-actions bg-red-500 hover:bg-red-600">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">Proceed to Checkout</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
