<%-- 
    Document   : addPromotion
    Created on : Mar 24, 2025, 9:06:59 PM
    Author     : THTN0
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<t:genericpage title="Add Promotion">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${sessionScope.shopId}";

            document.addEventListener("DOMContentLoaded", function () {
                fetchProductsShop(shopId, true);

                document.getElementById("promotionForm").addEventListener("submit", function (event) {
                    event.preventDefault();
                    submitPromotionForm();
                });
            });

            document.addEventListener("productsLoaded", function (event) {
                let productList = document.getElementById("productList");
                productList.innerHTML = "";

                event.detail.forEach(product => {
                    if (product.status) { // Chỉ hiển thị sản phẩm có status = true
                        productList.appendChild(createProductElement(product));
                    }
                });
            });

            function submitPromotionForm() {
                let form = document.getElementById("promotionForm");
                let selectedProducts = Array.from(document.querySelectorAll('.product-checkbox:checked'))
                        .map(checkbox => checkbox.value);

                if (selectedProducts.length === 0) {
                    alert("Please select at least one product for promotion.");
                    return;
                }

                let formData = new FormData(form);
                selectedProducts.forEach(productId => formData.append("selectedProducts", productId));

                fetch(contextPath + "/promotion", {
                    method: "POST",
                    body: formData
                })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                alert("Promotion added successfully!");
                                window.location.href = contextPath + "/sellercenter";
                            } else {
                                alert("Error: " + data.message);
                            }
                        })
                        .catch(error => {
                            console.error("Error submitting form:", error);
                            alert("An unexpected error occurred. Please try again.");
                        });
            }
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container mt-4">
            <h2 class="mb-3">Add Promotion</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form id="promotionForm" method="POST" action="${pageContext.request.contextPath}/promotion">
                <input type="hidden" id="creatorId" name="creatorId" value="${sessionScope.userId}">
                <div class="mb-3">
                    <label for="promotionName" class="form-label">Promotion Name</label>
                    <input type="text" id="promotionName" name="promotionName" class="form-control" required>
                </div>
                
                <div class="mb-3">
                    <label class="form-label">Promotion Type</label>
                    <select id="type" name="type" class="form-control">
                        <option value="0">Percentage (%)</option>
                        <option value="1">Flat Discount</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="value" class="form-label">Value</label>
                    <input type="number" id="discount" name="value" class="form-control" min="1" required>
                </div>

                <div class="mb-3">
                    <label for="startDate" class="form-label">Start Date</label>
                    <input type="date" id="startDate" name="startDate" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label for="exprireDate" class="form-label">End Date</label>
                    <input type="date" id="expireDate" name="expireDate" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Select Products</label>
                    <ul id="productList" class="list-unstyled border p-2" style="max-height: 300px; overflow-y: auto;"></ul>
                </div>

                <button type="submit" class="btn btn-primary">Create Promotion</button>
            </form>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
