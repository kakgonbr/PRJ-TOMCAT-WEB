<%-- POSSIBLE REQUEST ATTRIBUTES:  email, id--%>
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
        <p>TODO: COMPLETE THE PAGE.</p>
        <p>email: ${email}</p>
        <p>id: ${id}</p>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  /signup (POST) --%>
