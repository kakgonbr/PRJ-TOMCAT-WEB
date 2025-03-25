<%-- POSSIBLE REQUEST ATTRIBUTES:  products (list), resources (list) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="promotion" class="model.dto.PromotionDTO" scope="request"/>
<jsp:setProperty name="promotion" property="*"/>
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
        <form action="${pageContext.request.contextPath}/admin/cp?table=promotions" method="post">
            <table border="1" cellpadding="5">
                <c:if test="${promotion.id != null}">
                    <caption>
                        <h2>
                            Editing Promotion ID ${promotion.id}
                        </h2>
                    </caption>
                    <input type="hidden" name="id" value="<c:out value='${promotion.id}' />" />
                </c:if>
                <c:if test="${promotion.id == null}">
                    <caption>
                        <h2>
                            Creating a new promotion
                        </h2>
                     </caption>
                </c:if>
                <tr>
                    <th>Name:</th>
                    <td>
                        <input type="text" name="name" size="45" value="<c:out value='${promotion.name}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Value:</th>
                    <td>
                        <input type="text" name="value" size="45" value="<c:out value='${promotion.value}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Expire Date:</th>
                    <td>
                        <input type="text" name="expireDate" size="45" value="<c:out value='${promotion.expireDate}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Type:</th>
                    <td>
                        <select name="type">
                            <c:choose>
                                <c:when test="${promotion.type}">
                                    <option value="true">Flat</option>
                                    <option value="false">Percentage</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="false">Percentage</option>
                                    <option value="true">Flat</option>
                                </c:otherwise>
                            </c:choose>
                            
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>Is Of Admin:</th>
                    <td>
                        <select name="ofAdmin">
                            <c:choose>
                                <c:when test="${promotion.ofAdmin}">
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="false">No</option>
                                    <option value="true">Yes</option>
                                </c:otherwise>
                            </c:choose>
                            
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>Creation Date:</th>
                    <td>
                        <input type="text" name="creationDate" size="45" value="<c:out value='${promotion.creationDate}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Creator ID:</th>
                    <td>
                        <input type="text" name="creatorId" size="45" value="<c:out value='${promotion.creatorId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td>
                        <select name="status">
                            <c:choose>
                                <c:when test="${promotion.status}">
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="false">Inactive</option>
                                    <option value="true">Active</option>
                                </c:otherwise>
                            </c:choose>
                            
                        </select>
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
