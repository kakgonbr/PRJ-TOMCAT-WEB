<%-- POSSIBLE REQUEST ATTRIBUTES: reason --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
    <head>
    </head>

    <body>
        <h1>Test</h1>
        <t:template>
            <jsp:attribute name="footer">
                <t:footer/>
            </jsp:attribute>
        </t:template>
    </body>
</html>

<%-- POSSIBLE DESTINATIONS: /login (POST) --%>