<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        

        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container">
            <div class="row">
                <!-- Sidebar -->
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky ">

                        <!-- Accordion Menu -->
                        <div class="accordion fs-6" id="menuAccordion">
                            
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseProfile">
                                        Profile
                                    </button>
                                </h2>
                                <div id="collapseProfile" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/user" class="list-group-item list-group-item-action pb-3">Profile</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseOrder">
                                        Order History
                                    </button>
                                </h2>
                                <div id="collapseOrder" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/userorder?action=order" class="list-group-item list-group-item-action pb-3">Order </a>
                                        <a href="${pageContext.request.contextPath}/userorder?action=complete" class="list-group-item list-group-item-action">Order completed</a>
                                    </div>
                                </div>
                            </div>
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseShop">
                                        Notification
                                    </button>
                                </h2>
                                <div id="collapseShop" class="accordion-collapse collapse">
                                    <a href="#" class="list-group-item list-group-item-action p-3">Notification</a>
                                </div>
                            </div>

                        </div>
                    </div>
                </nav>
                <main class="col-10">
                    <h2>Order: </h2>
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Image</th>
                                <th>Product</th>
                                <th>Shop</th>
                                <th>Promotion</th>
                                <th>Quantity</th>
                                <th>Price (Per Item)</th>
                                <th>Customization</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="orderItem" items="${OrderItemList}">
                                <tr>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/resources/${orderItem.productWrapper.thumbnail}"
                                            alt="Product Image">
                                    </td>
                                    <td>${orderItem.productWrapper.name}</td>
                                    <td>${orderItem.productWrapper.shop.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${orderItem.productWrapper.promotion.type}">
                                                - ${orderItem.productWrapper.promotion.value} VND
                                            </c:when>
                                            <c:otherwise>
                                                - ${orderItem.productWrapper.promotion.value} %
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        ${orderItem.quantity}
                                    </td>
                                    <td>
                                        <!-- Someone optimize this, save to variables or something -->
                                        <c:choose>
                                            <c:when test="${orderItem.productWrapper.promotion != null && orderItem.productWrapper.promotion.type}">
                                                ${(orderItem.productItem.price - orderItem.productWrapper.promotion.value) * orderItem.quantity}
                                            </c:when>
                                            <c:when test="${orderItem.productWrapper.promotion != null && !orderItem.productWrapper.promotion.type}">
                                                ${(orderItem.productItem.price * (100.0 - orderItem.productWrapper.promotion.value) / 100.0) * orderItem.quantity}
                                            </c:when>
                                            <c:otherwise>
                                                ${orderItem.productItem.price * orderItem.quantity}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:forEach var="customization"
                                            items="${orderItem.productItem.customizations}">
                                            <p>${customization.name}: ${customization.value}
                                                ${customization.unit}</p>
                                        </c:forEach>
                                    </td>
                                </tr>
                                
                            </c:forEach>
                        </tbody>
                    </table>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>