<%-- POSSIBLE REQUEST ATTRIBUTES:  products (list), resources (list) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="shop" class="model.dto.ShopDTO" scope="request"/>
<jsp:setProperty name="shop" property="*"/>
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
        <form action="${pageContext.request.contextPath}/admin/cp?table=shops" method="post">
            <table border="1" cellpadding="5">
                <c:if test="${shop.id != null}">
                    <caption>
                        <h2>
                            Editing Shop ID ${shop.id}
                        </h2>
                    </caption>
                    <input type="hidden" name="id" value="<c:out value='${shop.id}' />" />
                </c:if>
                <c:if test="${shop.id == null}">
                    <caption>
                        <h2>
                            Creating a new shop
                        </h2>
                     </caption>
                </c:if>
                <tr>
                    <th>Name:</th>
                    <td>
                        <input type="text" name="name" size="45" value="<c:out value='${shop.name}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Address:</th>
                    <td>
                        <input type="text" name="address" size="45" value="<c:out value='${shop.address}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Is Visible:</th>
                    <td>
                        <input type="text" name="visible" size="45" value="<c:out value='${shop.visible}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Owner ID:</th>
                    <td>
                        <input type="text" name="ownerId" size="45" value="<c:out value='${shop.ownerId}' />" />
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
