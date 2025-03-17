<%-- POSSIBLE REQUEST ATTRIBUTES:  product (model.ProductDetailsWrapper) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="TEMPLATE">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav search="true" mainNav="true" user="${sessionScope.user.username}" activePage="home"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <c:if test="${error != null}">
            <h2>ERROR!!!!!!!!!!!!!!!!</h2>
        </c:if>
        <c:if test="${product != null && (error == null || error == '')}">
            <ul>
                <li>ID: ${product.id}</li>
                <li>Shop: 
                    <ul>
                        <li>ID: ${product.shop.id}</li>
                        <li>Name: ${product.shop.name}</li>
                        <li>Profile: <img src="${pageContext.request.contextPath}/resources/${product.shop.profileResource}" alt=""></li>
                    </ul>
                </li>
                <li>Category
                    <ul>
                        <li>ID: ${product.category.id}</li>
                        <li>Name: ${product.category.name}</li>
                    </ul>
                </li>
                <li>Name: ${product.name}</li>
                <li>Descriptipn: ${product.description}</li>
                <c:if test="${product.promotion != null}">
                    <li>Promotion
                        <ul>
                            <li>ID: ${product.promotion.id}</li>
                            <li>Name: ${product.promotion.name}</li>
                            <li>Value: ${product.promotion.value}</li>
                            <li>Expire: ${product.promotion.expireDate}</li>
                            <li>Type: ${product.promotion.type}</li>
                            <c:choose>
                                <c:when test="${product.promotion.type}">
                                    <li>Computed: - ${product.promotion.value} VND</li>
                                </c:when>
                                <c:otherwise>
                                     <li>Computed: - ${product.promotion.value} %</li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </li>
                </c:if>
                <li>Thumbnail: <img src="${pageContext.request.contextPath}/resources/${product.shop.profileResource}" alt=""></li>
                <li>Images:
                    <ul>
                        <c:forEach var="image" items="${product.productImages}">
                            <li> <img src="${pageContext.request.contextPath}/resources/${image}" alt=""></li></li>
                        </c:forEach>
                    </ul>
                </li>
                <c:if test="${not empty product.productCustomizations}">
                    <li>Customization: ${product.customizationName}
                        <ul>
                            <c:forEach var="customization" items="${product.productCustomizations}">
                                <li>ID: ${customization.id}
                                    <ul>
                                        <li>Name: ${customization.name}</li>
                                        <li>Stock: ${customization.stock}</li>
                                        <li>Price: ${customization.price}</li>
                                        <li>Value: ${customization.value}</li>
                                        <li>Unit: ${customization.unit}</li>
                                    </ul>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
            </ul>
        </c:if>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>
