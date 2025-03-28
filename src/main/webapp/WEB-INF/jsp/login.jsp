<%-- POSSIBLE REQUEST ATTRIBUTES: reason(invalid, forbidden) --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<t:genericpage title="Login">
    <jsp:attribute name="head">
        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>

        <t:resources />
        <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/main_css"> --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/login_css">   
    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <t:nav /> --%>
        <t:error error="${error}" />
        <t:error error="${param.reason}" />
    </jsp:attribute>

    <jsp:attribute name="body">
            <main class="container-fluid d-flex justify-content-evenly p-5"
                style="background-color: #9BB3C8;">
            <!--login-->
            <div style="width: 35%;">
                <img src="${pageContext.request.contextPath}/resources/logoBM_png" alt="shop_logo"
                    style="width: 100%;">
            </div>
            <!--form-->
            <div class="shadow rounded input-group-lg p-5" style="background-color: var(--bs-custom-content-focus) !important" id="formLogin">
                <div class="mb-5">
                    <h2 class="text-center">Login to your account</h2>
                </div>
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="form-floating">
                        <input type="text" class="form-control my-3 "
                            placeholder="Email address or username" id="floatingMail"
                            name="userOrEmail">
                        <label for="floatingMail">Email/Username</label>
                    </div>
                    <div class="form-floating">
                        <input type="password" class="form-control my-3" placeholder="Password"
                            id="floatingPassword" name="password">
                        <label for="floatingPassword">Password</label>
                    </div>
                    <div class="d-flex align-items-center justify-content-between my-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="rememberMe"
                                value="true" id="flexCheckDefault">
                            <label class="form-check-label text-muted" for="flexCheckDefault">
                                Remember me
                            </label>
                        </div>
                        <a href="#" class="text-decoration-none">
                            Forget password?
                        </a>
                    </div>
                    <input type="submit" class="btn btn-primary w-100 my-3" value="Sign in">
                </form>
            <div class="d-flex align-items-center my-2">
                <hr class="flex-grow-1 bg-secondary">
                <span class="mx-2 fs-6 text-muted">
                    or continue with
                </span>
                <hr class="flex-grow-1 bg-secondary">
            </div>
            <div class="row text-center my-4">
                <div class="col">
                    <a
                        href="https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https://kakgonbri.zapto.org:8443/prj/signup&prompt=select_account&response_type=code&client_id=691274987273-s4ooisq489ch7bp9ib1ttvp6cuhf1u96.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/userinfo.email&access_type=offline">
                        <button class="btn shadow w-75 custom-outline-button">
                            <i class="bi bi-google"></i>
                            Google
                        </button>
                    </a>
                </div>
                <div class="col">
                    <a
                        href="https://www.facebook.com/v22.0/dialog/oauth?fields=id,name,email&client_id=1292754858725836&redirect_uri=https://kakgonbri.zapto.org:8443/prj/signup?method=fb">
                        <button class="btn shadow w-75 custom-outline-button">
                            <i class="bi bi-facebook"></i>
                            Facebook
                        </button>
                    </a>
                </div>
            </div>
            <div class="d-flex align-items-center justify-content-center">
                <p class="text-muted my-0 mx-3">Don't have an account?</p>
                <a href="${pageContext.request.contextPath}/signup" class="text-decoration-none">
                    Create an account
                </a>
            </div>
            </div>
        </main>

    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS: /login (POST) --%>