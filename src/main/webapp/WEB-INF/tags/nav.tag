<%@tag description="The navbar bootstrap class" pageEncoding="UTF-8" %>
<%-- JSTL REQUIRED: pass an attribute (setting it not to be null) to enable the navigation element for that.--%>
<%-- For example, on the user login page, there shouldn't be a user dropdown, the user hasn't logged in yet.
--%>
<%@attribute name="search" required="false" %>
<%@attribute name="mainNav" required="false" %>
<%@attribute name="user" required="false" %>
<%@attribute name="activePage" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">BM Market</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
            data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown"
            aria-expanded="true" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="navbar-collapse collapse show" id="navbarNavDropdown">
            <ul class="navbar-nav me-auto">
                <c:if test="${mainNav != null}">
                    <li class="nav-item">
                        <a class="nav-link ${activePage == 'home' ? 'active' : ''}" href="${pageContext.request.contextPath}/home">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Another page here</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Yet another page</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            Dropdown link
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">Action</a></li>
                            <li><a class="dropdown-item" href="#">Another action</a></li>
                            <li>
                                <a class="dropdown-item" href="#">Something else here</a>
                            </li>
                        </ul>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <button
                        class="btn btn-link nav-link py-2 px-0 px-lg-2 dropdown-toggle d-flex align-items-center"
                        id="bd-theme" type="button" aria-expanded="false"
                        data-bs-toggle="dropdown" data-bs-display="static"
                        aria-label="Toggle theme (auto)">
                        Theme
                        <span class="d-lg-none ms-2" id="bd-theme-text">Toggle theme</span>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="bd-theme-text">
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
                                Dark
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
                </li>

                <c:if test="${search != null}">
                    <li>
                        <form class="d-flex" role="search">
                            <input class="form-control me-2" type="search" placeholder="Search"
                                aria-label="Search" />
                            <button class="btn btn-outline-success" type="submit">
                                Search
                            </button>
                        </form>
                    </li>
                </c:if>
                <c:if test="${user != null && user != ''}">
                    <li class="nav-item dropdown">
                        <!-- add user name here -->
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                            role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            ${user}
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end"
                            aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="#">Profile</a></li>
                            <li><a class="dropdown-item" href="#">Something</a></li>
                            <li>
                                <hr class="dropdown-divider" />
                            </li>
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/login?action=logout">Logout</a>
                            </li>
                        </ul>
                    </li>
                </c:if>
                <c:if test="${user == null || user == ''}">
                    <li>
                        <a href="${pageContext.request.contextPath}/login" class="btn shadow custom-outline-button">Log In</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>