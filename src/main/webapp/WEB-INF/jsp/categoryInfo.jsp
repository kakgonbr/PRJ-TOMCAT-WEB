<%-- POSSIBLE REQUEST ATTRIBUTES:  category (model.CategoryWrapper) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Category">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var categoryId = "${param.categoryId}"

            document.addEventListener("DOMContentLoaded", fetchByQueryAndCategory);
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="home"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <c:if test="${category != null}">
            <ul>Category:
                <t:categoryTree node="${category}" />
            </ul>
            <p>Products:</p>
            <table border="1" id="productTable">
            </table>
        </c:if>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>
