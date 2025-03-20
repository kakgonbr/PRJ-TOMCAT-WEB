<%-- POSSIBLE REQUEST ATTRIBUTES:  cartItems (model.CartItemWrapper) --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Cart">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">

    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Cart Items</h2>
        <table border="1">
            <c:forEach var="cartItem" items="cartItems">
                <th>
                    <td>Image</td>
                    <td>Name</td>
                    <td>Shop</td>
                    <td>Promotion</td>
                    <td>Quantity</td>
                    <td>Price</td>
                    <td>Stock</td>
                    <td>Customization</td>
                </th>
                <tbody>
                    <tr>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/${cartItem.productWrapper.thumbnail}" alt="">
                        </td>
                        <td>
                            ${cartItem.productWrapper.name}
                        </td>
                        <td>
                            ${cartItem.productWrapper.shop.name}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${cartItem.productWrapper.promotion.type}">
                                    - ${cartItem.productWrapper.promotion.value} VND
                                </c:when>
                                <c:otherwise>
                                    - ${cartItem.productWrapper.promotion.value} %
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            ${cartItem.quantity}
                        </td>
                        <td>
                            ${cartItem.productItem.price}
                        </td>
                        <td>
                            ${cartItem.productItem.stock}
                        </td>
                        <td>
                            <c:forEach var="customization" items="cartItem.productWrapper.customizations">
                                <p>${customization.name}: ${customization.value} ${customization.unit}</p><br></br>
                            </c:forEach>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/cart?action=remove&id=${cartItem.id}">Remove</a>
                        </td>
                    </tr>
                </tbody>
            </c:forEach>
        </table>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>

<%-- Useful implcit objects:
    ${pageContext.request.contextPath}
 --%>