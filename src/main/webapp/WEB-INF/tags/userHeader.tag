<%@tag description="Template tag" pageEncoding="UTF-8" %>

<%@attribute name="user" required="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<div class="container">
    <div class="row my-2">
        <div class="col-2 d-flex align-items-center">
            <a href="${pageContext.request.contextPath}/prj/shopauth"
                class="text-decoration-none text-dark fs-5">
                <i class="bi bi-shop"></i>
                <span class="text-muted"> Seller center</span>
            </a>
        </div>

        <a class="col-3 d-flex align-items-center justify-content-end " href="/prj/home">
            <p class="fs-2 fw-bold">BMMarket</p>
        </a>

        <div class="col-7 d-flex align-items-center justify-content-end">

            <div class="ms-2">
                <a class="p-2 text-decoration-none fs-5 logo rounded-pill" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-circle-half"></i>
                    <span class="text-muted">Theme</span>

                </a>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="bd-theme-text" style="">
                    <li>
                        <button type="button" id="button-theme-light"
                            class="dropdown-item d-flex align-items-center button-switch-theme"
                            data-bs-theme-value="light" aria-pressed="false">
                            Light
                        </button>
                    </li>
                    <li>
                        <button type="button" id="button-theme-dark"
                            class="dropdown-item d-flex align-items-center button-switch-theme"
                            data-bs-theme-value="dark" aria-pressed="false">
                            Dark (WIP)
                        </button>
                    </li>
                    <li>
                        <button type="button" id="button-theme-system"
                            class="dropdown-item d-flex align-items-center button-switch-theme"
                            data-bs-theme-value="system" aria-pressed="true">
                            Auto
                        </button>
                    </li>
                </ul>
            </div><!--notification-->
            <div class="ms-2">
                <a href="#" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                    <i class="bi bi-bell-fill"></i>
                    <span class="text-muted">Notification</span>
                </a>
            </div>

            <div class="ms-2">
                <a href="${pageContext.request.contextPath}/chat" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                    <i class="bi bi-chat-fill"></i>
                    <span class="text-muted">Message</span>
                </a>
            </div>

            <c:if test="${user != null && user != ''}">
                <div class="ms-2">
                    <!-- add user name here -->
                    <a class=" p-2 text-decoration-none fs-5 logo rounded-pill" href="#" id="navbarDropdown"
                        role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-fill "></i>
                        <span class="text-muted">${user}</span>

                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item"
                                href="${pageContext.request.contextPath}/user">Profile</a></li>
                        <li><a class="dropdown-item" href="#">Something</a></li>
                        <li>
                            <hr class="dropdown-divider" />
                        </li>
                        <li>
                            <a class="dropdown-item"
                                href="${pageContext.request.contextPath}/login?action=logout">Logout</a>
                        </li>
                    </ul>
                </div>
            </c:if>
            <c:if test="${user == null || user == ''}">
                <div class="ms-2">

                    <a href="${pageContext.request.contextPath}/login"
                        class="p-2 text-decoration-none fs-5 logo rounded-pill">Log In</a>

                </div>
            </c:if>
            <!--cart-->
            <div class="ms-2">
                <a href="${pageContext.request.contextPath}/cart" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                    <i class="bi bi-bag-fill"></i>
                    <span class="text-muted">Cart</span>
                </a>
            </div>
        </div>
    </div>
    <div class="row" style="position: relative;">
        <div class="col-2 fs-5 fw-semibold dropdown dropdown-full-width" data-bs-toggle="dropdown"
            aria-expanded="false" type="button" id="category">
            <i class="bi bi-list"></i>
            <span>CATEGORY</span>
        </div>
        <div class="dropdown-menu border-0 shadow p-3 overflow-auto my-3 dropdown-menu-hover">
            <div class="container">
                <div class="nav nav-tabs d-flex align-items-center justify-content-center text-dark">
                    <ul class="nav nav-tabs" id="categoryTabs"></ul>
                </div>
                <div class="row">
                    <div class="tab-content"></div>
                </div>
            </div>
        </div>
        <div class="col-7 mx-auto">
            <form class="input-group ms-2 rounded y w-75" style="background-color: rgb(248, 246, 246);"
                action="${pageContext.request.contextPath}/search">
                <input type="text" class="form-control border-0 rounded"
                    placeholder="Search for items and brands" aria-label="Search" data-bs-toggle="dropdown"
                    aria-expanded="false" style="background-color: rgb(248, 246, 246);" name="query"
                    id="searchBar" autocomplete="off">
                <input type="hidden" name="categoryId">
                <div class="btn  border-0 rounded clear-btn">
                    <i class="bi bi-x-circle-fill"></i>
                </div>
                <button class="btn border-0 rounded-5 noHoverEffect" type="submit">
                    <i class="bi bi-search"></i>
                </button>
            </form>

        </div>
    </div>
    <t:error error="${error}"/>
</div>