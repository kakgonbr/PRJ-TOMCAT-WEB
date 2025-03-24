<%-- 
    Document   : displayPromotion
    Created on : Mar 24, 2025
    Author     : hoahtm
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<t:genericpage title="Manage Promotions">
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
                <!-- Sidebar -->
<!--                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky">
                        <input type="text" id="searchBox" class="form-control my-3" placeholder="Quick access...">

                        <div class="accordion fs-6" id="menuAccordion">
                             Promotion Section 
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapsePromotion">
                                        Promotions
                                    </button>
                                </h2>
                                <div id="collapsePromotion" class="accordion-collapse collapse show">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/promotion" class="list-group-item list-group-item-action">🎁 View Promotions</a>
                                        <a href="${pageContext.request.contextPath}/addpromotion" class="list-group-item list-group-item-action">➕ Add Promotion</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>-->

                <!-- Main Content -->
                <main class="col-md-10 p-4">
                    <div class="container">
                        <h2>Promotion List</h2>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>

                        <div class="mb-3">
                            <a href="${pageContext.request.contextPath}/sellercenter/promotion" class="btn btn-primary">➕ Add Promotion</a>
                        </div>

                        <c:if test="${not empty promotions}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Type</th>
                                        <th>Value</th>
                                        <th>Start Date</th> 
                                        <th>Expire Date</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="promo" items="${promotions}">
                                        <tr>
                                            <td>${promo.id}</td>
                                            <td>${promo.name}</td>
                                            <td>${promo.type ? "Percentage" : "Fixed Amount"}</td>
                                            <td>${promo.value}</td>
                                            <td>${promo.creationDate}</td> 
                                            <td>${promo.expireDate}</td>
                                            <td>${promo.status ? "Active" : "Inactive"}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                        <c:if test="${empty promotions}">
                            <p>No promotions available.</p>
                        </c:if>
                    </div>
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

