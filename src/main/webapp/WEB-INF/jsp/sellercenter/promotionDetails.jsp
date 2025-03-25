<%-- 
    Document   : promotionDetails
    Created on : Mar 24, 2025
    Author     : hoahtm
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<t:genericpage title="Promotion Details">
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
                        <h2>Promotion: ${param.promotionId}</h2>

                        <button type="button" class="btn btn-primary my-3" data-bs-toggle="modal" data-bs-target="#addProductModal">
                            ➕ Add Product
                        </button>

                        <!-- Modal hiển thị danh sách sản phẩm chưa có promotion -->
                        <div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addProductModalLabel">Select Products to Add</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form id="addProductForm" method="POST" action="${pageContext.request.contextPath}/sellercenter/promotion?action=addProductPromotion">
                                            <input type="hidden" name="promotionId" value="${param.promotionId}">

                                            <c:if test="${empty unpromotedProducts}">
                                                <p>No available products to add.</p>
                                            </c:if>

                                            <c:if test="${not empty unpromotedProducts}">
                                                <table class="table">
                                                    <thead>
                                                        <tr>
                                                            <th>Select</th>
                                                            <th>Product ID</th>
                                                            <th>Name</th>
                                                            <th>Description</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="product" items="${unpromotedProducts}">
                                                            <tr>
                                                                <td><input type="checkbox" name="productIds" value="${product.id}"></td>
                                                                <td>${product.id}</td>
                                                                <td>${product.name}</td>
                                                                <td>${product.description}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:if>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <button type="submit" class="btn btn-success" id="submitBtn" onclick="submitAddProductForm()">Add to Promotion</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <script>
                            function submitAddProductForm() {
                                document.getElementById("submitBtn").disabled = true;
                                document.getElementById("addProductForm").submit();
                            }
                        </script>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>

                        <c:if test="${not empty products}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Product ID</th>
                                        <th>Shop ID</th>
                                        <th>Category ID</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Image</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="product" items="${products}">
                                        <c:if test="${product.status}">
                                            <tr>
                                                <td>${product.id}</td>
                                                <td>${product.shopId.id}</td>
                                                <td>${product.categoryId.id}</td>
                                                <td>${product.name}</td>
                                                <td>${product.description}</td>
                                                <td><img src="${product.imageStringResourceId}" alt="${product.name}" width="50"></td>
                                                <td>${product.status}</td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                        <c:if test="${empty products}">
                            <p>No products found for this promotion.</p>
                        </c:if>

                        <a href="${pageContext.request.contextPath}/sellercenter/promotion" class="btn btn-secondary">Back to Promotions</a>
                    </div>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
