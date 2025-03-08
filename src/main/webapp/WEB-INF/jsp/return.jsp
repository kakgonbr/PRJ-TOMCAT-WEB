<%-- POSSIBLE REQUEST ATTRIBUTES:  status --%>
<%-- POSSIBLE REQUEST PARAMETERS:  vnp_TxnRef, vnp_Amount, vnp_OrderInfo, vnp_ResponseCode, vnp_TransactionNo, vnp_BankCode, vnp_PayDate --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Payment Result">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>PAYMENT RESULT</h1>
        <p>Status: ${status}</p>
        <c:if test="${status != 'Invalid signature'}">
            <p>Payment Code: ${param[vnp_TxnRef]}</p>
            <p>Amount: ${param[vnp_Amount]} VND</p>
            <p>Order Information: ${param[vnp_OrderInfo]}</p>
            <p>Response Code: ${param[vnp_ResponseCode]}</p>
            <p>Bank Code: ${param[vnp_BankCode]}</p>
            <p>Payment Date: ${param[vnp_PayDate]}</p>
        </c:if>
    </jsp:attribute>

    <jsp:attribute name="body">

    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>

<%-- Useful implcit objects:
    ${pageContext.request.contextPath}
 --%>