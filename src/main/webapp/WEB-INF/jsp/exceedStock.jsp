<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Cart">
    <jsp:attribute name="head">
        <t:resources/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/cart_css">
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container rounded my-3 p-3">
            <h2 class="text-xl font-bold mb-4">The following items have invalid quantity:</h2>
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
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cartItem" items="${exceed}">
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
                                ${cartItem.quantity}
                            </td>
                            <td>${cartItem.productItem.price}</td>
                            <td>${cartItem.productItem.stock}</td>
                            <td>
                                <c:forEach var="customization" items="${cartItem.productItem.customizations}">
                                    <p>${customization.name}: ${customization.value} ${customization.unit}</p>
                                </c:forEach>
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
