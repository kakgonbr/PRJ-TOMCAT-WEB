<t:genericpage title="Header">
    <jsp:attribute name="head">
        <t:resources/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/userMain_css">
        <script src="${pageContext.request.contextPath}/resources/userMain_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        
    </jsp:attribute>

    <jsp:attribute name="body">
        
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>