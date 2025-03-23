<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <c:set var="total" value="0" />
        <h1>Order Review:</h1>
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
                        <c:choose>
                            <c:when test="${cartItem.productWrapper.promotion.type}">
                                <c:set var="total" value="${total + (cartItem.productItem.price - cartItem.productWrapper.promotion.value)}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="total" value="${total + (cartItem.productItem.price * (100.0 - cartItem.productWrapper.promotion.value) / 100.0)}" />
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tbody>
            </table>
        <form>
            <div class="address-container">
                <label for="address">Địa chỉ</label>
                <input type="text" id="address" name="address" required placeholder="Nhập địa chỉ của bạn" autocomplete="off">
                <div id="suggestions" class="suggestions"></div>
            </div>
        </form>
        <form action="${pageContext.request.contextPath}/checkout" method="POST">
            <input type="hidden" name="action" value="apply">
            <select name="promotionId">
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
                        </c:choose>] ${promotion.name} - Expires on ${promotion.expireDate}</option>
                </c:forEach>
            </select>
            <button type="submit">Apply</button>
        </form>
        <c:choose>
            <c:when test="${activePromotion == null}">
                <p>Final Price: ${total}</p>
            </c:when>
            <c:otherwise>
                <p>Active promotion: [
                        <c:choose>
                            <c:when test="${activePromotion.type}">
                                - ${activePromotion.value} VND
                            </c:when>
                            <c:otherwise>
                                - ${activePromotion.value} %
                            </c:otherwise>
                        </c:choose>] ${activePromotion.name} - Expires on ${activePromotion.expireDate}</p>
                <c:choose>
                    <c:when test="${activePromotion.type}">
                        <p>Final Price: ${total - activePromotion.value}</p>
                    </c:when>
                    <c:otherwise>
                        <p>Final Price: ${total * (100.0 - activePromotion.value) / 100.0}</p>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <form action="${pageContext.request.contextPath}/checkout" method="POST">
            <input type="hidden" name="action" value="proceed">
            <c:if test="${activePromotion != null}">
                <input type="hidden" name="promotionId" value="${activePromotion.id}">
            </c:if>
            <button type="submit">Proceed to Payment</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
        <script>
            const addressInput = document.getElementById('address');
            const suggestionsContainer = document.getElementById('suggestions');

            function debounce(func, wait) {
                let timeout;
                return function executedFunc(...args) {
                    clearTimeout(timeout);
                    timeout= setTimeout(() => {clearTimeout(timeout);func(...args);}, wait);
                };
            }

            const search = debounce((query) =>
            {
                if(query.length < 2 || /^\W+$/.test(query)) {
                    suggestionsContainer.style.display= 'none';
                    return;
                }
                
                //var url = new URL('https://' + location.host + contextPath + '/ajax/map?action=auto&query=' + encodeURIComponent(query));
                let url= 'https://kakgonbri.zapto.org:8443/prj/ajax/map?action=auto&query='+encodeURIComponent(query);
                fetch(url)
                .then(response => response.json())
                .then(data => 
                {
                    if(data.status === 'OK') {
                        suggestionsContainer.innerHTML='';
                        suggestionsContainer.style.display= 'block';
                        data.predictions.forEach(prediction => 
                        {
                            const div = document.createElement('div');
                            div.className = 'suggestion-item';
                            div.textContent = prediction.description;
                            div.addEventListener('click', () => 
                            {
                                addressInput.value = prediction.description;
                                suggestionsContainer.style.display = 'none';
                            });
                            suggestionsContainer.appendChild(div);
                        });
                    }
                })  
                .catch(error => console.error('Error:', error));
            },300);

            addressInput.addEventListener('input', (e) => search(e.target.value));
        </script>
    </jsp:attribute>
</t:genericpage>