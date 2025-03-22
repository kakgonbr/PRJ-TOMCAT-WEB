<%-- POSSIBLE REQUEST ATTRIBUTES: email, id, error, taken --%>
<%-- POSSIBLE REQUEST PARAMETERS: username, email, phoneNumber, password --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Sign Up">
    <jsp:attribute name="head">
        <t:resources/>
    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- TODO: Add informative error messages telling the user accepted formats --%>
        <c:if test="${error == 'db'}">
            <h1>A DATABASE ERROR OCCURRED!</h1>
        </c:if>
        <c:if test="${error == 'username'}">
            <h1>USERNAME ERROR!</h1>
        </c:if>
        <c:if test="${error == 'email'}">
            <h1>EMAIL ERROR!</h1>
        </c:if>
        <c:if test="${error == 'password'}">
            <h1>PASSWORD ERROR!</h1>
        </c:if>
        <c:if test="${error == 'phoneNumber'}">
            <h1>PHONE NUMBER ERROR!</h1>
        </c:if>
        <c:if test="${taken == 'username'}">
            <h1>USERNAME IS TAKEN!</h1>
        </c:if>
        <c:if test="${taken == 'email'}">
            <h1>EMAIL IS TAKEN!</h1>
        </c:if>
        <c:if test="${taken == 'phoneNumber'}">
            <h1>PHONE NUMBER IS TAKEN!</h1>
        </c:if>
        <h1>Sign up</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="row vh-100">
                <!-- left container -->
                <div class="col-md-8 ps-5 pe-5">
                    <div class="w-auto text-end mt-5">
                        <a href="#" class="text-decoration-none" style="color: #6c63ff;">Already a member?
                            <i class="bx bxs-user-circle" style="color: #6c63ff;"></i>
                        </a>
                    </div>
                    <div class="mt-5 d-flex justify-content-between">
                        <h2 class="d-inline-block w-100 me-5">Input your information</h2>
                        <p class="text-muted fs-6">
                            We need you to help us with some basic information for your account creation.
                            Here are our <strong class="fw-bold">terms and conditions</strong>. Please read them carefully.
                        </p>
                    </div>
                    <hr class="border-dark" style="border-style: dashed;">

                    <!-- form -->
                    <form action="${pageContext.request.contextPath}/signup" method="POST">
                        <div class="row d-flex justify-content-between">
                            <input type="hidden" name="googleId" value="${googleId}">
                            <p class="fs-0" style="color: #d7b3ff">Component</p>

                            <!-- Username -->
                            <div class="col-md-6 mb-4 pe-5">
                                <p>Username:</p>
                                <input type="text" name="username" value="${param.username}" class="form-control">
                            </div>

                            <!-- Email -->
                            <div class="col-md-6 mb-4 ps-5">
                                <p>Work on email:</p>
                                <c:choose>
                                    <c:when test="${empty googleId}">
                                        <input type="text" name="email" value="${param.email}" class="form-control">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" name="email" value="${email}" disabled class="form-control">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="row d-flex justify-content-between">
                            <!-- Password -->
                            <div class="col-md-6 mb-4 pe-5">
                                <p>Password:</p>
                                <input type="password" name="password" value="${param.password}" class="form-control">
                            </div>

                            <!-- Phone Number -->
                            <div class="col-md-6 mb-4 ps-5">
                                <p>Phone Number:</p>
                                <input type="text" name="phoneNumber" value="${param.phoneNumber}" class="form-control">
                            </div>
                        </div>
                        <!-- end form -->

                        <hr class="border-dark" style="border-style: dashed;">
                        <div class="d-flex justify-content-center">
                            <button type="submit" class="btn btn-dark mt-5">Register</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS: /signup (POST) --%>