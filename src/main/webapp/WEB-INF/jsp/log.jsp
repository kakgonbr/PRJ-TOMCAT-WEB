<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<t:genericpage title="TEMPLATE">
    <jsp:attribute name="head">
        <t:resources/>
        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>
        <script src="${pageContext.request.contextPath}/resources/log_js"></script>
    </jsp:attribute>

    <jsp:attribute name="header">
        
    </jsp:attribute>

    <jsp:attribute name="body">
    <h2>LOG!!!!!!!!!</h2>
    <pre id="logContainer" style="background: #f4f4f4; padding: 10px; border: 1px solid #ccc; max-height: 500px; overflow-y: auto;"></pre>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>
