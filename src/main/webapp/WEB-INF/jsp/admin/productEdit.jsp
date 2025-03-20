<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="product" class="model.dto.ProductDTO" scope="request"/>
<jsp:setProperty name="product" property="*"/>
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
        <form action="${pageContext.request.contextPath}/admin/cp?table=products" method="post">
            <table border="1" cellpadding="5">
                <c:if test="${product.id != null}">
                    <caption>
                        <h2>
                            Editing Product ID ${product.id}
                        </h2>
                    </caption>
                    <input type="hidden" name="id" value="<c:out value='${product.id}' />" />
                </c:if>
                <c:if test="${product.id == null}">
                    <caption>
                        <h2>
                            Creating a new product
                        </h2>
                     </caption>
                </c:if>
                <tr>
                    <th>Name:</th>
                    <td>
                        <input type="text" name="name" size="45" value="<c:out value='${product.name}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Descriptipn:</th>
                    <td>
                        <input type="text" name="description" size="45" value="<c:out value='${product.description}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Category ID:</th>
                    <td>
                        <input type="text" name="categoryId" size="45" value="<c:out value='${product.categoryId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Available Promotion ID:</th>
                    <td>
                        <input type="text" name="availablePromotionId" size="45" value="<c:out value='${product.availablePromotionId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Image String Resource ID:</th>
                    <td>
                        <input type="text" name="imageStringResourceId" size="45" value="<c:out value='${product.imageStringResourceId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Shop ID:</th>
                    <td>
                        <input type="text" name="shopId" size="45" value="<c:out value='${product.shopId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td>
                        <input type="text" name="status" size="45" value="<c:out value='${product.status}' />" />
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
