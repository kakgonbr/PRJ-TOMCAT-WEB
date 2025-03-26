 <%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Home">
    <jsp:attribute name="head">
        <t:resources/>
        <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/main_css"> --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/newHome_css">

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        <script src="${pageContext.request.contextPath}/resources/newHome_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            document.addEventListener("DOMContentLoaded", fetchProductsHomePage);
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <main class="container mt-4">
            <div class="row">
                <div class="">
                    <div id="carouselExampleAutoplaying" class="carousel slide rounded" data-bs-ride="carousel">
                        <div class="carousel-inner rounded">
                          <div class="carousel-item active">
                            <!--can be a link. the image can be different -->
                            <img src="https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=1999&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" class="d-block border rounded" alt="..." style="width: 100%; height: 550px; object-fit: cover;">
                          </div>
                          <div class="carousel-item">
                            <img src="https://images.pexels.com/photos/90946/pexels-photo-90946.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" class="d-block border rounded" alt="..." style="width: 100%; height: 550px; object-fit: cover;">
                          </div>
                          <div class="carousel-item">
                            <img src="https://images.pexels.com/photos/15191189/pexels-photo-15191189/free-photo-of-collection-of-cosmetic-products-in-a-pink-storage-bin.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" class="d-block border rounded" alt="..." style="width: 100%; height: 550px; object-fit: cover;">
                          </div> 
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
                          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                          <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
                          <span class="carousel-control-next-icon" aria-hidden="true"></span>
                          <span class="visually-hidden">Next</span>
                        </button>
                    </div>
                </div>
                
            </div>
            <!--infinite slider-->
            <div class="slider mt-3 " reverse="true" style="--width: 200px; --height: 200px; --quantity:12;">
                <div class="list" id="product-slider">
                    <!-- Products will be added here dynamically -->
                </div>
            </div>
            <h3 class="my-5 text-center">Suggested for you</h3>
            <div class="row gy-4">
                <!-- Product cards will be inserted here dynamically -->
            </div>
        </main>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>