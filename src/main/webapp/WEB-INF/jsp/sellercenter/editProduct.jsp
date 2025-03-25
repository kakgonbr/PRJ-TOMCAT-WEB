<%-- Document : editProduct Created on : Mar 20, 2025, 11:36:46 PM Author : hoahtm --%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Edit product">
    <jsp:attribute name="head">
        <t:resources />
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
                handleAccordionSearch("searchBox", "menuAccordion");
            });
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <t:nav search="true" mainNav="true" user="${sessionScope.user.username}"
            activePage="shophome" /> --%>
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container-fluid">

            <div class="row">
                <!-- Sidebar -->
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky ">
                        <!-- Quick Access Search -->
                        <input type="text" id="searchBox" class="form-control my-3"
                            placeholder="Quick access...">

                        <!-- Accordion Menu -->
                        <div class="accordion fs-6" id="menuAccordion">


                            <!-- Product Section -->
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button"
                                        data-bs-toggle="collapse" data-bs-target="#collapseProduct">
                                        Product
                                    </button>
                                </h2>
                                <div id="collapseProduct" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/sellercenter/shophome"
                                            class="list-group-item list-group-item-action pb-3">🛍 My
                                            Products</a>
                                        <a href="${pageContext.request.contextPath}/addproduct"
                                            class="list-group-item list-group-item-action">➕ Add
                                            Product</a>
                                    </div>
                                </div>
                            </div>

                            <!-- Shop -->
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button"
                                        data-bs-toggle="collapse" data-bs-target="#collapseShop">
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

                        <div class="card">
                            <h4>Thumbnail:</h4>
                            <img src="${pageContext.request.contextPath}/resources/${product.thumbnail}"
                                class="thumbnail-img" alt="Thumbnail">
                            <form
                                action="${pageContext.request.contextPath}/product?action=setThumbnail&productId=${product.id}"
                                enctype="multipart/form-data" method="POST">
                                <input type="file" class="form-control" name="image">
                                <button type="submit" class="btn btn-primary">Update</button>
                            </form>
                        </div>

                        <div class="card">
                            <h4>Images:</h4>
                            <div class="row">
                                <c:forEach var="image" items="${product.productImages}">
                                    <div class="col-md-3">
                                        <div class="text-center">
                                            <img src="${pageContext.request.contextPath}/resources/${image}"
                                                class="product-img" alt="Product Image">
                                            <form
                                                action="${pageContext.request.contextPath}/product?action=removePicture&productId=${product.id}"
                                                method="POST">
                                                <input type="hidden" name="imageId" value="${image}">
                                                <button type="submit"
                                                    class="btn btn-danger btn-sm">Remove</button>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="card">
                            <h4>Add Picture:</h4>
                            <form
                                action="${pageContext.request.contextPath}/product?action=addPicture&productId=${product.id}"
                                enctype="multipart/form-data" method="POST">
                                <input type="file" class="form-control" name="image">
                                <button type="submit" class="btn btn-success">Add Picture</button>
                            </form>
                        </div>

                        <div class="card">
                            <c:choose>
                                <c:when test="${product.promotion == null}">
                                    <h4>Add Promotion:</h4>
                                    <form
                                        action="${pageContext.request.contextPath}/product?action=addPromotion&productId=${product.id}"
                                        method="POST">
                                        <label class="form-label">Name:</label>
                                        <input type="text" class="form-control" name="name" required>

                                        <label class="form-label">Type: Check for flat, uncheck for
                                            %</label>
                                        <input type="checkbox" class="form-check-input" name="type"
                                            value="true">

                                        <label class="form-label">Value:</label>
                                        <input type="number" class="form-control" name="value">

                                        <label class="form-label">Expire On:</label>
                                        <input type="text" class="form-control" name="expire"
                                            placeholder="dd/mm/yyyy">

                                        <button type="submit" class="btn btn-warning mt-2">Add
                                            Promotion</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <h4>Current Promotion:</h4>
                                    <ul class="list-group">
                                        <li class="list-group-item">Name: ${product.promotion.name}</li>
                                        <li class="list-group-item">Value:
                                            <c:choose>
                                                <c:when test="${product.promotion.type}">
                                                    - ${product.promotion.value} VND
                                                </c:when>
                                                <c:otherwise>
                                                    - ${product.promotion.value} %
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                        <li class="list-group-item">Expire On:
                                            ${product.promotion.expireDate}</li>
                                    </ul>
                                    <form
                                        action="${pageContext.request.contextPath}/product?action=removePromotion&productId=${product.id}"
                                        method="POST">
                                        <button type="submit" class="btn btn-danger mt-2">Remove
                                            Promotion</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <form
                            action="${pageContext.request.contextPath}/product?action=edit&productId=${product.id}"
                            method="post">
                            <input type="hidden" name="id" value="${product.id}" />

                            <div class="mb-3">
                                <label for="name" class="form-label">name:</label>
                                <input type="text" id="name" name="name" class="form-control"
                                    value="${product.name}" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Description:</label>
                                <textarea id="description" name="description" class="form-control"
                                    required>${product.description}</textarea>
                            </div>

                            <div class="mb-3">
                                <label>Category: ${product.category.name}</label>
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
                                            <tr>
                                                <td>
                                                    <input type="hidden" name="productItemId"
                                                        value="${item.id}">
                                                    ${item.id}
                                                </td>
                                                <td>
                                                    <input type="number" name="stock"
                                                        value="${item.stock}">
                                                </td>
                                                <td>
                                                    <input type="text" name="price"
                                                        value="${item.price}">
                                                </td>
                                                <td>
                                                    <c:forEach var="customization"
                                                        items="${item.customizations}">
                                                        ${customization.name} : ${customization.value}
                                                        ${customization.unit}
                                                        <br></br>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="mb-3">
                                <button type="submit" class="btn btn-primary">Save changes</button>
                                <a href="${pageContext.request.contextPath}/sellercenter/shophome"
                                    class="btn btn-secondary">Hủy</a>
                            </div>
                        </form>
                    </div>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>