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

    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="row">
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky">
                        <input type="text" id="searchBox" class="form-control my-3" placeholder="Quick access...">
                        <div class="accordion fs-6" id="menuAccordion">
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOrder">
                                        Order
                                    </button>
                                </h2>
                                <div id="collapseOrder" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/sellercenter/order" class="list-group-item list-group-item-action pb-3">📦 My Order</a>
                                    </div>
                                </div>
                            </div>
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseProduct">
                                        Product
                                    </button>
                                </h2>
                                <div id="collapseProduct" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/sellercenter/shophome" class="list-group-item list-group-item-action pb-3">🛍 My Products</a>
                                        <a href="${pageContext.request.contextPath}/sellercenter/addproduct" class="list-group-item list-group-item-action">➕ Add Product</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>
                <main class="col-md-10 p-4">
                    <div class="container mt-4">

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form id="promotionForm" method="POST" action="${pageContext.request.contextPath}/sellercenter/promotion/addPromotion">

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
                                        <input type="date" name="expireDate" size="45" value="<c:out value='${promotion.expireDate}' />" />
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
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
