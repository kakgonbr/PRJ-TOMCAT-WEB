<%-- 
    Document   : editProduct
    Created on : Mar 20, 2025, 11:36:46 PM
    Author     : hoahtm
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<t:genericpage title="Edit product">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
                handleAccordionSearch("searchBox", "menuAccordion");
            });
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="shophome"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container-fluid">

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
                    <div class="container">
                        <h2>Edit product</h2>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/product?action=edit&productId=${product.id}" method="post">
                            <input type="hidden" name="id" value="${product.id}" /> 

                            <div class="mb-3">
                                <label for="name" class="form-label">name:</label>
                                <input type="text" id="name" name="name" class="form-control" value="${product.name}" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Description:</label>
                                <textarea id="description" name="description" class="form-control" required>${product.description}</textarea>
                            </div>

                            <div class="mb-3">
                                <label>Category:</label>
                                <div id="categoryFilter"></div>
                            </div>

                            <div class="mb-3">
                                <h4>product item</h4>
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Stock</th>
                                            <th>Price</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${product.productItems}">
                                            <c:out value="${product.productItems}" />
                                            <tr>
                                                <td>${item.id}</td>
                                                <input type="hidden" name="productItemId" value="${item.id}">
                                                <td>
                                                    <input type="text" name="productItemStock[]" class="form-control" value="${item.stock}" required>
                                                </td>
                                                <td>
                                                    <input type="text" name="productItemPrice[]" class="form-control" value="${item.price}" required step="0.01">
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="mb-3">
                                <button type="submit" class="btn btn-primary">Save changes</button>
                                <a href="${pageContext.request.contextPath}/shophome" class="btn btn-secondary">Hủy</a>
                            </div>
                        </form>
                    </div>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
