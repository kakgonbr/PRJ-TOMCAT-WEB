<%-- 
    Document   : promotionDetails
    Created on : Mar 25, 2025, 7:14:32 PM
    Author     : THTN0
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
        <div class="container">
            <h2>Promotion: ${param.promotionId}</h2>
            
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
                                    <td>${product.shopId}</td>
                                    <td>${product.categoryId}</td>
                                    <td>${product.name}</td>
                                    <td>${product.description}</td>
                                    <td><img src="${product.imageStringResourceId}" alt="${product.name}" width="50"></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            
            <c:if test="${empty products}">
                <p>No products found for this promotion.</p>
            </c:if>

            <a href="${pageContext.request.contextPath}/promotion" class="btn btn-secondary">Back to Promotions</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

