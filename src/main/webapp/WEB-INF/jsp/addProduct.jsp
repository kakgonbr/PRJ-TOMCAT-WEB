<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${sessionScope.shopId}";

            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
            });
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="addProduct"/>
    </jsp:attribute>

    <jsp:attribute name="body">
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
                                    <a href="#" class="list-group-item list-group-item-action pb-3">🛍 My
                                        Products</a>
                                    <a href="${pageContext.request.contextPath}/addproduct" class="list-group-item list-group-item-action">➕ Add Product</a>
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
                    </div>
                </div>
            </nav>
            <main class="col-md-10 p-4">
                <c:if test="${not empty error}">
                    <div>${error}</div>
                </c:if>

                <form id="addProductForm" action="${pageContext.request.contextPath}/addproduct" method="post">
                    <input type="hidden" name="action" value="addProduct">

                    <label for="productName">Product Name:</label>
                    <input type="text" id="productName" name="name" required><br>

                    <label for="description">Description:</label>
                    <textarea id="description" name="description"></textarea><br>
                    <label>Category:</label>
                    <div id="categoryFilter"></div> <%-- Danh sách danh mục hiển thị tại đây --%>
                    <button type="submit">Add Product</button>
                </form>
            </main>

            <%-- Hiển thị lỗi nếu có --%>

        </jsp:attribute>

        <jsp:attribute name="footer">
            <t:footer/>
        </jsp:attribute>
    </t:genericpage>





















