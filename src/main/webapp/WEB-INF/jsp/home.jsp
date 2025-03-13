<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Home">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            document.addEventListener("DOMContentLoaded", fetchProducts);
            document.addEventListener("DOMContentLoaded", fetchCategory);
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>Home</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>Welcome.</p>
        <div id="categoryFilter"></div>
        <input type="text" id="searchBox" placeholder="Query">
        <button onclick="fetchProducts()">Search</button>
        <p>Products:</p>
        <table border="1" id="productTable">
        </table>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>