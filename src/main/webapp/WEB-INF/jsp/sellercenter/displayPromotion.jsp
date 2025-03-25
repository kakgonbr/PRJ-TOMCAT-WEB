<%-- 
    Document   : displayPromotion
    Created on : Mar 24, 2025
    Author     : hoahtm
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<t:genericpage title="Manage Promotions">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="row">
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky ">
                        <!-- Quick Access Search -->
                        <input type="text" id="searchBox" class="form-control my-3" placeholder="Quick access...">

                        <!-- Accordion Menu -->
                        <div class="accordion fs-6" id="menuAccordion">
                            <!-- Order Section -->
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseOrder">
                                        Order
                                    </button>
                                </h2>
                                <div id="collapseOrder" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/sellercenter/order" class="list-group-item list-group-item-action pb-3">📦 My
                                            order</a>
                                        <!--                                    <a href="../html/return-refund-cancle.html" class="list-group-item list-group-item-action fs-6">🔄
                                                                                Return/Refund/Cancel</a>-->
                                    </div>
                                </div>
                            </div>
                            <!-- Product Section -->
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseProduct">
                                        Product
                                    </button>
                                </h2>
                                <div id="collapseProduct" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/sellercenter/shophome" class="list-group-item list-group-item-action pb-3">🛍 My
                                            Products</a>
                                        <a href="${pageContext.request.contextPath}/sellercenter/addproduct" class="list-group-item list-group-item-action">➕ Add Product</a>
                                    </div>
                                </div>
                            </div>

                            <!-- Shop -->
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseShop">
                                        Shop
                                    </button>
                                </h2>
                                <div id="collapseShop" class="accordion-collapse collapse">
                                    <a href="${pageContext.request.contextPath}/shop?shopId=${sessionScope.shopId}" class="list-group-item list-group-item-action p-3">🏪    
                                        Shop
                                        Information</a>
                                </div>
                            </div>
                            <!-- marketing center -->
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseMarketing">
                                        Marketing center
                                    </button>
                                </h2>
                                <div id="collapseMarketing" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="shop-advertisement.html"
                                           class="list-group-item list-group-item-action pb-3">📢
                                            Shop Advertisement</a>
                                        <a href="${pageContext.request.contextPath}/sellercenter/promotion" 
                                           class="list-group-item list-group-item-action">🏷 Discount</a>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </nav>
                <main class="col-md-10 p-4">
                    <div class="container">
                        <h2>Promotion List</h2>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>

                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/sellercenter/promotion?action=add" class="btn btn-primary">➕ Add Promotion</a>
                        </div>

                        <c:if test="${not empty promotions}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Type</th>
                                        <th>Value</th>
                                        <th>Start Date</th> 
                                        <th>Expire Date</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="promo" items="${promotions}">
                                        <tr>
                                            <td>${promo.id}</td>
                                            <td>${promo.name}</td>
                                            <td>${promo.type ? "Flat" : "Percentage"}</td>
                                            <td>${promo.value}</td>
                                            <td>${promo.creationDate}</td> 
                                            <td>${promo.expireDate}</td>
                                            <td>${promo.status ? "Active" : "Inactive"}</td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/sellercenter/promotion?action=products&promotionId=${promo.id}" 
                                                   class="btn btn-primary btn-sm">
                                                    View Details
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                        <c:if test="${empty promotions}">
                            <p>No promotions available.</p>
                        </c:if>
                    </div>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
