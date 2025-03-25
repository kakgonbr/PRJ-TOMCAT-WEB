<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        <script src="${pageContext.request.contextPath}/resources/addProduct_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${sessionScope.shopId}";

            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
            });
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="row">
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar">
                    <div class="position-sticky">
                        <input type="text" id="searchBox" class="form-control my-3" placeholder="Quick access...">
                        <div class="accordion" id="menuAccordion">
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseProduct">
                                        Product
                                    </button>
                                </h2>
                                <div id="collapseProduct" class="accordion-collapse collapse show">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/sellercenter/shophome" class="list-group-item list-group-item-action">🛍 My Products</a>
                                        <a href="${pageContext.request.contextPath}/sellercenter/addproduct" class="list-group-item list-group-item-action active">➕ Add Product</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>

                <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                    <div class="container mt-4">
                        <div class="card shadow p-4">
                            <h2 class="text-center mb-4">Add Product</h2>
                            <form id="addProductForm" action="${pageContext.request.contextPath}/sellercenter/addproduct" method="post">
                                <div class="mb-3">
                                    <label for="productName" class="form-label">Product Name:</label>
                                    <input type="text" id="productName" name="name" class="form-control" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="description" class="form-label">Description:</label>
                                    <textarea id="description" name="description" class="form-control"></textarea>
                                </div>
                                
                                <div class="mb-3">
                                    <label class="form-label">Category:</label>
                                    <div id="categoryFilter"></div>
                                    <button type="button" class="btn btn-outline-primary mt-2" onclick="onSelectCategory()">Continue</button>
                                </div>
                                
                                <div class="mb-3">
                                    <h5>Product Variations</h5>
                                    <ul id="variation-container" class="list-group"></ul>
                                    <table id="variation-table" class="table table-bordered mt-3">
                                        <thead>
                                            <tr>
                                                <th>Product Item</th>
                                                <th>Stock</th>
                                                <th>Price</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                    <button type="button" class="btn btn-outline-secondary" onclick="addProductItem()">Add product item</button>
                                </div>
                                
                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary px-5">Add Product</button>
                                </div>
                            </form>
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




















<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        <script src="${pageContext.request.contextPath}/resources/addProduct_js"></script>

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
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Add Product</h2>

        <form id="addProductForm" action="${pageContext.request.contextPath}/sellercenter/addproduct" method="post">
            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="name" required><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea><br>
            <label>Category:</label>
            <div id="categoryFilter"></div>  Danh sách danh mục hiển thị tại đây 
            <label onclick="onSelectCategory()">Continue</label>
            <ul id="variation-container">
            
            </ul>
            <table id="variation-table" border="1">
                <thead>
                    <tr>
                    <th>Product Item</th>
                    <th>Stock</th>
                    <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            <table>
            <label onclick="addProductItem()">Add product item</label>
            <button type="submit">Add</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>--%>