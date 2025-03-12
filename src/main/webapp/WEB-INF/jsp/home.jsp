<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Home">
    <jsp:attribute name="head">
        <t:resources/>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            window.onload = fetchProducts;
        </script>

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>Home</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>Welcome.</p>
        <p>Products:</p>
        <ul id="list"></ul>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>