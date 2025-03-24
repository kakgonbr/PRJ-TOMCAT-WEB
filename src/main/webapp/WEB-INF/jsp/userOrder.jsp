<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        

        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="container">
            <div class="row">
                <!-- Sidebar -->
                <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-none sidebar">
                    <div class="position-sticky ">

                        <!-- Accordion Menu -->
                        <div class="accordion fs-6" id="menuAccordion">
                            
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseProfile">
                                        Profile
                                    </button>
                                </h2>
                                <div id="collapseProfile" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/user" class="list-group-item list-group-item-action pb-3">Profile</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseOrder">
                                        Order History
                                    </button>
                                </h2>
                                <div id="collapseOrder" class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <a href="${pageContext.request.contextPath}/userorder?action=order" class="list-group-item list-group-item-action pb-3">Order </a>
                                        <a href="${pageContext.request.contextPath}/userorder?action=complete" class="list-group-item list-group-item-action">Order completed</a>
                                    </div>
                                </div>
                            </div>
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapseShop">
                                        Notification
                                    </button>
                                </h2>
                                <div id="collapseShop" class="accordion-collapse collapse">
                                    <a href="#" class="list-group-item list-group-item-action p-3">Notification</a>
                                </div>
                            </div>

                        </div>
                    </div>
                </nav>
                <main class="col-10">
                    <h2>Order: </h2>
                    
                </main>
            </div>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>