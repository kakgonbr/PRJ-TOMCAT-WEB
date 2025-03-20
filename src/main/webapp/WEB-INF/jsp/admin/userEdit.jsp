<%-- POSSIBLE REQUEST ATTRIBUTES:  products (list), resources (list) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="user" class="model.dto.UserDTO" scope="request"/>
<jsp:setProperty name="user" property="*"/>
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
        <form action="${pageContext.request.contextPath}/admin/cp?table=users" method="post">
            <table border="1" cellpadding="5">
                <c:if test="${user.id != null}"> 
                    <caption>
                        <h2>
                            Editing User ID ${user.id}
                        </h2>
                    </caption>
                    <input type="hidden" name="id" value="<c:out value='${user.id}' />" />
                </c:if>
                <c:if test="${user.id == null}">
                    <caption>
                        <h2>
                            Creating a new user
                        </h2>
                     </caption>
                </c:if>
                <tr>
                    <th>Email:</th>
                    <td>
                        <input type="text" name="email" size="45" value="<c:out value='${user.email}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Username:</th>
                    <td>
                        <input type="text" name="username" size="45" value="<c:out value='${user.username}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Phone Number:</th>
                    <td>
                        <input type="text" name="phoneNumber" size="45" value="<c:out value='${user.phoneNumber}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Password:</th>
                    <td>
                        <input type="text" name="password" size="45" value="<c:out value='${user.password}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Persistent Cookie:</th>
                    <td>
                        <input type="text" name="persistentCookie" size="45" value="<c:out value='${user.persistentCookie}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Google ID:</th>
                    <td>
                        <input type="text" name="googleId" size="45" value="<c:out value='${user.googleId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Facebook ID:</th>
                    <td>
                        <input type="text" name="facebookId" size="45" value="<c:out value='${user.facebookId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Display Name:</th>
                    <td>
                        <input type="text" name="displayName" size="45" value="<c:out value='${user.displayName}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Bio:</th>
                    <td>
                        <input type="text" name="bio" size="45" value="<c:out value='${user.bio}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Is Admin:</th>
                    <td>
                        <input type="text" name="isAdmin" size="45" value="<c:out value='${user.isAdmin}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Credit:</th>
                    <td>
                        <input type="text" name="credit" size="45" value="<c:out value='${user.credit}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Profile String Resource ID:</th>
                    <td>
                        <input type="text" name="profileStringResourceId" size="45" value="<c:out value='${user.profileStringResourceId}' />" />
                    </td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td>
                        <input type="text" name="status" size="45" value="<c:out value='${user.status}' />" />
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
