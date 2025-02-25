<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Admin Statistics">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>Admin page.</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>admin?</p>
        <form action="${pageContext.request.contextPath}/admin" method="POST">
            <input type="hidden" name="action" value="enableMaintenance"/>
            <input type="submit" value="Enable Maintenance"/>
        </form>
        <form action="${pageContext.request.contextPath}/admin" method="POST">
            <input type="hidden" name="action" value="disableMaintenance"/>
            <input type="submit" value="Disable Maintenance"/>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>