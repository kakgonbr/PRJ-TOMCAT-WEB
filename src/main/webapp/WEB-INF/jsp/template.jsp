<%-- Template file, unreachable. Delete this comment after copying. --%>
<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="TEMPLATE">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>TEMPLATE HEADING</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>THE CONTENT OF THE TEMPLATE JSP.</p>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>

<%-- Useful implcit objects:
    ${pageContext.request.contextPath}
 --%>