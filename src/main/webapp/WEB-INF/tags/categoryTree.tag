<%@ attribute name="node" required="true" type="model.CategoryWrapper" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<li>
ID: ${node.ID}
</li>
<li>
    Name: ${node.name}
    Image: <img src="${pageContext.request.contextPath}/resources/${node.resourceString}" alt="">
    <c:if test="${not empty node.children}">
        <ul>
            <c:forEach var="child" items="${node.children}">
                <t:categoryTree node="${child}"/>
            </c:forEach>
        </ul>
    </c:if>
</li>