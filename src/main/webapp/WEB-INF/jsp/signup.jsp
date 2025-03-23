<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Sign Up">
    <jsp:attribute name="head">
        <t:resources/>
        <style>
            /* Reset CSS */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: "Poppins", sans-serif;
            }

            /* Body */
            body {
                background: linear-gradient(135deg, #f3f4f6, #d1d5db);
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }

            /* Title */
            .title {
                font-size: 28px;
                font-weight: bold;
                color: #333;
                margin-bottom: 20px;
            }

            /* Signup Form */
            .signup-form {
                background: white;
                padding: 25px;
                width: 380px;
                border-radius: 12px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
                text-align: center;
            }

            .signup-form label {
                display: block;
                font-weight: 600;
                text-align: left;
                margin-top: 12px;
                color: #333;
            }

            /* Input Fields */
            .signup-form input {
                width: 100%;
                padding: 12px;
                margin-top: 5px;
                border: 1px solid #ccc;
                border-radius: 8px;
                font-size: 14px;
            }

            .signup-form input:focus {
                outline: none;
                border-color: #007BFF;
                box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
            }

            /* Submit Button */
            .submit-btn {
                background: #007BFF;
                color: white;
                border: none;
                padding: 12px;
                margin-top: 20px;
                width: 100%;
                border-radius: 8px;
                cursor: pointer;
                font-size: 16px;
                transition: 0.3s;
            }

            .submit-btn:hover {
                background: #0056b3;
            }

            /* Error Messages */
            .error-container {
                text-align: center;
                margin-bottom: 15px;
            }

            .error-message {
                color: red;
                font-weight: bold;
                font-size: 14px;
            }

            /* Footer */
            .footer {
                margin-top: 20px;
                font-size: 12px;
                color: #666;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <div class="error-container">
            <c:if test="${error == 'db'}">
                <p class="error-message">A DATABASE ERROR OCCURRED!</p>
            </c:if>
            <c:if test="${error == 'username'}">
                <p class="error-message">Invalid Username!</p>
            </c:if>
            <c:if test="${error == 'email'}">
                <p class="error-message">Invalid Email!</p>
            </c:if>
            <c:if test="${error == 'password'}">
                <p class="error-message">Invalid Password!</p>
            </c:if>
            <c:if test="${error == 'phoneNumber'}">
                <p class="error-message">Invalid Phone Number!</p>
            </c:if>
            <c:if test="${taken == 'username'}">
                <p class="error-message">Username is already taken!</p>
            </c:if>
            <c:if test="${taken == 'email'}">
                <p class="error-message">Email is already taken!</p>
            </c:if>
            <c:if test="${taken == 'phoneNumber'}">
                <p class="error-message">Phone number is already taken!</p>
            </c:if>
        </div> --%>
        <t:error error="${error}" />
        <h1 class="title">Sign Up</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <form action="${pageContext.request.contextPath}/signup" method="POST" class="signup-form">
            <input type="hidden" name="googleId" value="${googleId}">
            
            <label>Username:</label>
            <input type="text" name="username" value="${param.username}" required>

            <label>Email:</label>
            <c:if test="${googleId == null || googleId == ''}">
                <input type="email" name="email" value="${param.email}" required>
            </c:if>
            <c:if test="${googleId != null && googleId != ''}">
                <input type="email" name="email" value="${email}" disabled>
            </c:if>

            <label>Phone Number:</label>
            <input type="tel" name="phoneNumber" value="${param.phoneNumber}" required>

            <label>Password:</label>
            <input type="password" name="password" value="${param.password}" required>

            <input type="submit" value="Sign up" class="submit-btn" />
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <div class="footer">
        </div>
       <div class="row text-center my-4">
    <div class="col">
        <a href="https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https://kakgonbri.zapto.org:8443/prj/signup&prompt=select_account&response_type=code&client_id=691274987273-s4ooisq489ch7bp9ib1ttvp6cuhf1u96.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/userinfo.email&access_type=offline">
            <button class="btn btn-google shadow w-75 custom-outline-button">
                <i class="bi bi-google me-2"></i> Google
            </button>
        </a>
    </div>
    <div class="col">
        <a href="https://www.facebook.com/v22.0/dialog/oauth?fields=id,name,email&client_id=1292754858725836&redirect_uri=https://kakgonbri.zapto.org:8443/prj/signup?method=fb">
            <button class="btn btn-facebook shadow w-75 custom-outline-button">
                <i class="bi bi-facebook me-2"></i> Facebook
            </button>
        </a>
    </div>
</div>

    </jsp:attribute>
</t:genericpage>
