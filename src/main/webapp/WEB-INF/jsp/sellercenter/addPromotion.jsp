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
                
                <table border="1" cellpadding="5">
                    <c:if test="${promotion.id != null}">
                        <caption>
                            <h2>
                                Editing Promotion ID ${promotion.id}
                            </h2>
                        </caption>
                        <input type="hidden" name="id" value="<c:out value='${promotion.id}' />" />
                    </c:if>
                    <c:if test="${promotion.id == null}">
                        <caption>
                            <h2>
                                Creating a new promotion
                            </h2>
                        </caption>
                    </c:if>
                    <input type="hidden" id="creatorId" name="creatorId" value="${sessionScope.user.id}">
                    <tr>
                        <th>Name:</th>
                        <td>
                            <input type="text" name="name" size="45" value="<c:out value='${promotion.name}' />" />
                        </td>
                    </tr>
                    <tr>
                        <th>Value:</th>
                        <td>
                            <input type="text" name="value" size="45" value="<c:out value='${promotion.value}' />" />
                        </td>
                    </tr>
                    <tr>
                        <th>Expire Date:</th>
                        <td>
                            <input type="text" name="expireDate" size="45" value="<c:out value='${promotion.expireDate}' />" />
                        </td>
                    </tr>
                    <tr>
                        <th>Type:</th>
                        <td>
                            <select name="type">
                                <c:choose>
                                    <c:when test="${promotion.type}">
                                        <option value="true">Flat</option>
                                        <option value="false">Percentage</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="false">Percentage</option>
                                        <option value="true">Flat</option>
                                    </c:otherwise>
                                </c:choose>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>Creation Date:</th>
                        <td>
                            <input type="text" name="creationDate" size="45" value="<c:out value='${promotion.creationDate}' />" />
                        </td>
                    </tr>
                    <tr>
                        <th>Status:</th>
                        <td>
                            <select name="status">
                                <c:choose>
                                    <c:when test="${promotion.status}">
                                        <option value="true">Active</option>
                                        <option value="false">Inactive</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="false">Inactive</option>
                                        <option value="true">Active</option>
                                    </c:otherwise>
                                </c:choose>

                            </select>
                        </td>
                    </tr>
                    <tr>

                        <td colspan="2" align="center">
                            <input type="submit" value="Save" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
