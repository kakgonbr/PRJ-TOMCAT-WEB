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
        <%-- <h1>PAYMENT RESULT</h1>
        <p>Status: ${status}</p>
        <c:if test="${status != 'Invalid signature'}">
            <p>Payment Code: ${param.vnp_TxnRef}</p>
            <p>Amount: ${param.vnp_Amount / 100.0} VND</p>
            <p>Order Information: ${param.vnp_OrderInfo}</p>
            <p>Response Code: ${param.vnp_ResponseCode}</p>
            <p>Bank Code: ${param.vnp_BankCode}</p>
            <p>Payment Date: ${param.vnp_PayDate}</p>
        </c:if> --%>
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container text-center mt-5">
            <div class="card shadow-lg p-4">
                <h1 class="text-success">Order Completed!</h1>
                <p class="lead">Thank you for your purchase. Your order has been successfully processed.</p>
                <p class="lead">Amount paid: ${param.vnp_Amount / 100.0}</p>
                <a href="${pageContext.request.contextPath}/userorder?action=complete" class="btn btn-primary mt-3">View Order Details</a>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>

<%-- Useful implcit objects:
    ${pageContext.request.contextPath}
 --%>