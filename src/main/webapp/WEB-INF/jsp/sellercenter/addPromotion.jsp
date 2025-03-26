<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<t:genericpage title="Add Promotion">
    <jsp:attribute name="head">
        <t:resources />
        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
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

                <!-- Main Content -->
                <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                    <div class="container mt-4">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <div class="card shadow-lg p-4">
                            <h2 class="text-center mb-4">
                                <c:choose>
                                    <c:when test="${promotion.id != null}">
                                        Editing Promotion ID ${promotion.id}
                                    </c:when>
                                    <c:otherwise>
                                        Creating a New Promotion
                                    </c:otherwise>
                                </c:choose>
                            </h2>

                            <form id="promotionForm" method="POST" action="${pageContext.request.contextPath}/sellercenter/promotion?action=addPromotion">
                                <input type="hidden" name="creatorId" value="${sessionScope.user.id}">
                                <c:if test="${promotion.id != null}">
                                    <input type="hidden" name="id" value="<c:out value='${promotion.id}' />" />
                                </c:if>

                                <div class="mb-3">
                                    <label class="form-label">Name:</label>
                                    <input type="text" name="name" class="form-control" value="<c:out value='${promotion.name}' />" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Value:</label>
                                    <input type="number" name="value" class="form-control" value="<c:out value='${promotion.value}' />" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Expire Date:</label>
                                    <input type="text" name="expireDate" class="form-control" value="<c:out value='${promotion.expireDate}' />" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Type:</label>
                                    <select name="type" class="form-select">
                                        <option value="true" ${promotion.type ? 'selected' : ''}>Flat</option>
                                        <option value="false" ${!promotion.type ? 'selected' : ''}>Percentage</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Status:</label>
                                    <select name="status" class="form-select">
                                        <option value="true" ${promotion.status ? 'selected' : ''}>Active</option>
                                        <option value="false" ${!promotion.status ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary px-5">Save</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>
