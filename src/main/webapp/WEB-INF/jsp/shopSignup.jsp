<%-- 
    Document   : shopSignup
    Created on : Mar 18, 2025, 10:40:05 PM
    Author     : hoahtm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<t:genericpage title="ShopSignup">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <c:if test="${error == 'db'}">
            <h1>A DATABASE ERROR OCCURRED!</h1>
        </c:if>
        <c:if test="${error == 'shopName'}">
            <h1>shopName invalid!</h1>
        </c:if>
        <c:if test="${error == 'address'}">
            <h1>address invalid!</h1>
        </c:if>
    </jsp:attribute>

    <jsp:attribute name="body">
        <main class="container-fluid"
              <!--shop signup-->

              <div class="m-4 pt-5">
                <form action="${pageContext.request.contextPath}/shop-signup" method="POST">
                    <div class="d-flex justify-content-evenly">
                        <h2 class="pt-4">Input your information</h2>
                        <p class="text-muted fs-6 w-25">We need you to help us with some basic information for your account
                            creation. Here are our <strong class="fw-bold text-info-emphasis">terms and conditions</strong>.
                            Please
                            read them
                            carefully</p>
                    </div>
                    <div class="d-flex justify-content-center">
                        <hr class="d-flex justify-content-center w-75 mb-5 mt-4" style="border-style: dashed;">
                    </div>
                    <div class="row">

                        <div class="col-md-6 d-flex flex-column align-items-center">
                            <p class="align-self-center w-50">Shop Name</p>
                            <input type="text" class="form-control w-50 mb-4" value="${param.shopName}">
                            <p class="align-self-center w-50">Address:</p>
                            <input type="text" class="form-control w-50 mb-4" value="${param.address}">>
                        </div>
                        <div class="col-md-6">
                            <p class="text-dark">i dont know what to write Here</p>
                        </div>
                    </div>
                    <div class="d-grid gap-2 col-2 mx-auto mt-4">
                            <button class="btn btn-primary w-100" type="submit">Register</button>
                    </div>
                </form>
            </div>
        </main>

    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
        <h2><a href="${pageContext.request.contextPath}/redirect?page=log">View Log</a></h2>
    </jsp:attribute>
</t:genericpage>
