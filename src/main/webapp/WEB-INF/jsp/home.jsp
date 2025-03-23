<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Home">
    <jsp:attribute name="head">
        <t:resources/>
        <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/main_css"> --%>
        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            document.addEventListener("DOMContentLoaded", fetchCategory);
            document.addEventListener("DOMContentLoaded", fetchByQueryAndCategory);
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>Welcome.</p>
        <div id="categoryFilter"></div>
        <input type="text" id="searchBox" placeholder="Query">
        <button onclick="fetchByQueryAndCategory()">Search</button>
        <p>Products:</p>
        <table border="1" id="productTable">
        </table>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
        <script src="${pageContext.request.contextPath}/resources/userMain_js"></script>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>