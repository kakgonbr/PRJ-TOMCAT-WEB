<%-- POSSIBLE REQUEST ATTRIBUTES:  products (list), resources (list) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Admin Control Panel">
    <jsp:attribute name="head">
        <t:resources/>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>
        

        <%-- <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="${pageContext.request.contextPath}/resources/chart_js"></script>
        <script src="${pageContext.request.contextPath}/resources/admin_js"></script>
        <script src="${pageContext.request.contextPath}/resources/log_js"></script> --%>
        
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin_css">
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>Admin page.</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Product List</h2>
        <table border="1">

        <tr>
            <th>ID</th>
            <th>Path</th>
        </tr>
        <c:forEach var="resource" items="${resources}" varStatus="status">
            <c:if test="${status.index >= start && status.index < end}">
                <tr>
                    <td>${resource.id}</td>
                    <td>${resource.systemPath}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                            <input type="hidden" name="systemPath" value="${resource.systemPath}">
                            <input type="hidden" name="table" value="resources">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="id" value="${resource.id}">
                            <button type="submit">Edit</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                            <input type="hidden" name="table" value="resources">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${resource.id}">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
        
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  ${pageContext.request.contextPath}/admin/cp?table=products&action=edit&id=1--%>
        <%-- <table border="1">

        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Description</th>
            <th>CategoryId</th>
            <th>availablePromotionId</th>
            <th>imageStringResourceId</th>
            <th>shopId</th>
        </tr>
        <c:forEach var="product" items="${products}" varStatus="status">
            <c:if test="${status.index >= start && status.index < end}">
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.price}</td>
                    <td>${product.description}</td>
                    <td>${product.categoryId}</td>
                    <td>${product.availablePromotionId}</td>
                    <td>${product.imageStringResourceId}</td>
                    <td>${product.shopId}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                            <input type="hidden" name="table" value="products">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="id" value="${product.id}">
                            <button type="submit">Edit</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                            <input type="hidden" name="table" value="products">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${product.id}">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </table> --%>