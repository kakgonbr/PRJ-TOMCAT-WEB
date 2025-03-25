<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:genericpage title="Order">
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
                    <h2>Đơn hàng của bạn:</h2>
                    
                    <c:forEach var="orderItem" items="${OrderItemList}" varStatus="status">
                        <c:if test="${status.index == 0 || OrderItemList[status.index-1].orderId != orderItem.orderId}">
                            <!-- Đầu mỗi đơn hàng mới -->
                            <div class="card mb-4">
                                <div class="card-header bg-primary text-white">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="mb-0">Đơn hàng #${orderItem.orderId}</h5>
                                        <span><fmt:formatDate value="${orderItem.date}" pattern="dd-MM-yyyy HH:mm"/></span>
                                    </div>
                                </div>
                                <div class="card-body">
                        </c:if>
                        
                        <!-- Chi tiết từng sản phẩm trong đơn -->
                        <div class="row mb-3 p-3 border-bottom">
                            <div class="col-2">
                                <img src="${pageContext.request.contextPath}/resources/${orderItem.productWrapper.thumbnail}"
                                     alt="Product Image" class="img-fluid">
                            </div>
                            <div class="col-6">
                                <h6>${orderItem.productWrapper.name}</h6>
                                <p class="text-muted mb-1">Cửa hàng: ${orderItem.productWrapper.shop.name}</p>
                                <div class="small">
                                    <c:forEach var="customization" items="${orderItem.productItem.customizations}">
                                        <span class="badge bg-secondary me-1">
                                            ${customization.name}: ${customization.value} ${customization.unit}
                                        </span>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="col-2 text-center">
                                <p class="mb-1">Số lượng: ${orderItem.quantity}</p>
                                <c:if test="${orderItem.productWrapper.promotion != null}">
                                    <p class="text-danger mb-1">
                                        <c:choose>
                                            <c:when test="${orderItem.productWrapper.promotion.type}">
                                                Giảm ${orderItem.productWrapper.promotion.value}đ
                                            </c:when>
                                            <c:otherwise>
                                                Giảm ${orderItem.productWrapper.promotion.value}%
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </c:if>
                            </div>
                            <div class="col-2 text-end">
                                <h6>
                                    <c:choose>
                                        <c:when test="${orderItem.productWrapper.promotion != null && orderItem.productWrapper.promotion.type}">
                                            ${(orderItem.productItem.price - orderItem.productWrapper.promotion.value) * orderItem.quantity}đ
                                        </c:when>
                                        <c:when test="${orderItem.productWrapper.promotion != null && !orderItem.productWrapper.promotion.type}">
                                            ${(orderItem.productItem.price * (100.0 - orderItem.productWrapper.promotion.value) / 100.0) * orderItem.quantity}đ
                                        </c:when>
                                        <c:otherwise>
                                            ${orderItem.productItem.price * orderItem.quantity}đ
                                        </c:otherwise>
                                    </c:choose>
                                </h6>
                            </div>
                        </div>

                        <c:if test="${status.last || OrderItemList[status.index+1].orderId != orderItem.orderId}">
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                    
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>