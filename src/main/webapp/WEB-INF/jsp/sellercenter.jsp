<%-- 
    Document   : sellercenter
    Created on : Mar 19, 2025, 4:28:21 PM
    Author     : THTN0
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<t:genericpage title="Seller Center">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${sessionScope.shopId}";
            document.addEventListener("DOMContentLoaded", function () {
                fetchProductsShop(shopId, true);
                handleAccordionSearch("searchBox", "menuAccordion");
            });
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="shophome"/> --%>
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>Request URI: ${pageContext.request.requestURI}</p>
        <div class="container-fluid">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="row">
                <!-- Sidebar -->
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky ">
                        <!-- Quick Access Search -->
                        <input type="text" id="searchBox" class="form-control my-3" placeholder="Quick access...">

                        <!-- Accordion Menu -->
                        <div class="accordion fs-6" id="menuAccordion">


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
                                    <a href="#" class="list-group-item list-group-item-action p-3">🏪    
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
                                        <a href="discount.html" class="list-group-item list-group-item-action">🏷
                                            Discount</a>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </nav>
                <main class="col-md-10 p-4">
                    <div class="tab-content mt-3">
                        <div id="all" class="tab-pane fade show active">
                            <table class="table table-bordered" id="productTableShop">
                                <button onclick="fetchProductsShop(shopId, true);" class="btn btn-primary">View Available Products</button>
                                <button onclick="fetchProductsShop(shopId, false);" class="btn btn-danger">View Deleted Products</button>
                            </table>
                        </div>
                    </div>
                </main>

            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
