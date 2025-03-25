<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Checkout">
    <jsp:attribute name="head">
        <t:resources />
        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/checkout_css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/cart_css">
        <script src="${pageContext.request.contextPath}/resources/checkout_js"></script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <c:set var="total" value="0" />
        <div class="container rounded my-3 p-3 cart-container">
            <h2 class="text-xl font-bold mb-4">Order Review:</h2>
            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Image</th>
                        <th>Product</th>
                        <th>Shop</th>
                        <th>Promotion</th>
                        <th>Quantity</th>
                        <th>Price (Per Item)</th>
                        <th>Stock</th>
                        <th>Customization</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cartItem" items="${cartItems}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/resources/${cartItem.productWrapper.thumbnail}"
                                    alt="Product Image">
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
                            <td>
                                <!-- Someone optimize this, save to variables or something -->
                                <c:choose>
                                    <c:when test="${cartItem.productWrapper.promotion != null && cartItem.productWrapper.promotion.type}">
                                        ${(cartItem.productItem.price - cartItem.productWrapper.promotion.value)}
                                    </c:when>
                                    <c:when test="${cartItem.productWrapper.promotion != null && !cartItem.productWrapper.promotion.type}">
                                        ${(cartItem.productItem.price * (100.0 - cartItem.productWrapper.promotion.value) / 100.0)}
                                    </c:when>
                                    <c:otherwise>
                                        ${cartItem.productItem.price}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${cartItem.productItem.stock}</td>
                            <td>
                                <c:forEach var="customization"
                                    items="${cartItem.productItem.customizations}">
                                    <p>${customization.name}: ${customization.value}
                                        ${customization.unit}</p>
                                </c:forEach>
                            </td>
                        </tr>
                        <c:choose>
                            <c:when test="${cartItem.productWrapper.promotion == null}">
                                <c:set var="total" value="${total + cartItem.productItem.price * cartItem.quantity}"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${cartItem.productWrapper.promotion.type}">
                                        <c:set var="total"
                                            value="${(total + (cartItem.productItem.price - cartItem.productWrapper.promotion.value) * cartItem.quantity)}" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="total"
                                            value="${(total + (cartItem.productItem.price * (100.0 - cartItem.productWrapper.promotion.value) / 100.0) * cartItem.quantity)}" />
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="container">
            <form class="mb-3">
                <div class="mb-3">
                    <label for="address" class="form-label">Address</label>
                    <input type="text" id="address" name="address" class="form-control" required placeholder="Enter your address" autocomplete="off">
                    <div id="suggestions" class="form-text"></div>
                </div>
            </form>

            <form action="${pageContext.request.contextPath}/checkout" method="POST" class="mb-3">
                <input type="hidden" name="action" value="apply">
                
                <div class="mb-3">
                    <label for="promotionId" class="form-label">Select Promotion</label>
                    <select name="promotionId" id="promotionId" class="form-select">
                        <option value="">None</option>
                        <c:forEach var="promotion" items="${promotions}">
                            <option value="${promotion.id}">[
                                <c:choose>
                                    <c:when test="${promotion.type}">
                                        - ${promotion.value} VND
                                    </c:when>
                                    <c:otherwise>
                                        - ${promotion.value} %
                                    </c:otherwise>
                                </c:choose>] ${promotion.name} - Expires on ${promotion.expireDate}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">Apply</button>
            </form>

            <c:choose>
                <c:when test="${activePromotion == null}">
                    <p class="fw-bold">Final Price: <span class="text-primary">${total}</span></p>
                </c:when>
                <c:otherwise>
                    <p class="fw-bold text-success">Active Promotion: [
                        <c:choose>
                            <c:when test="${activePromotion.type}">
                                - ${activePromotion.value} VND
                            </c:when>
                            <c:otherwise>
                                - ${activePromotion.value} %
                            </c:otherwise>
                        </c:choose>] ${activePromotion.name} - Expires on ${activePromotion.expireDate}
                    </p>

                    <c:choose>
                        <c:when test="${activePromotion.type}">
                            <p class="fw-bold">Final Price: <span class="text-success">${total - activePromotion.value}</span></p>
                        </c:when>
                        <c:otherwise>
                            <p class="fw-bold">Final Price: <span class="text-success">${total * (100.0 - activePromotion.value) / 100.0}</span></p>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>

            <form action="${pageContext.request.contextPath}/checkout" method="POST">
                <input type="hidden" name="action" value="proceed">
                <input type="hidden" name="address" id="hiddenAddress">
                
                <c:if test="${activePromotion != null}">
                    <input type="hidden" name="promotionId" value="${activePromotion.id}">
                </c:if>

                <button type="submit" class="btn btn-success" onclick="validateAddress()">Proceed to Payment</button>
            </form>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
        
    </jsp:attribute>
</t:genericpage>