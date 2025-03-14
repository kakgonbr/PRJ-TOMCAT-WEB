<%-- POSSIBLE REQUEST ATTRIBUTES:  products (list), resources (list) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="resource" class="model.dto.ResourceDTO" scope="request"/>
<jsp:setProperty name="resource" property="*"/>
<%-- Serialize the object into a json string, send it to the servlet to turn into an object and send it here. --%>

<t:genericpage title="Admin's Edit Page">
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
        <div align="center">
        <form action="${pageContext.request.contextPath}/admin/cp?table=resources&action=edit" method="post">
            <table border="1" cellpadding="5">
                <caption>
                    <h2>
                        Editing Resource ID ${resource.id}
                    </h2>
                </caption>
                <c:if test="${resource != null}">
                    <input type="hidden" name="id" value="<c:out value='${resource.id}' />" />
                </c:if>
                <tr>
                    <th>System Path:</th>
                    <td>
                        <input type="text" name="systemPath" size="45" value="<c:out value='${resource.systemPath}' />" />
                    </td>
                </tr>
                <tr>

                    <td colspan="2" align="center">
                        <input type="submit" value="Save" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
    </table>
        
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  ${pageContext.request.contextPath}/admin/cp?table=products&action=edit&id=1--%>
