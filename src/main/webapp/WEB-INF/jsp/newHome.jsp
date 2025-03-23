<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Home">
    <jsp:attribute name="head">
        <t:resources/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/Main_css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/newHome_css">

        <script src="${pageContext.request.contextPath}/resources/product_js"></script>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        
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
                            <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" class="d-block border rounded" alt="..." style="width: 100%; height: 550px; object-fit: cover;">
                          </div>
                          <div class="carousel-item">
                            <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" class="d-block border rounded" alt="..." style="width: 100%; height: 550px; object-fit: cover;">
                          </div>
                          <div class="carousel-item">
                            <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" class="d-block border rounded" alt="..." style="width: 100%; height: 550px; object-fit: cover;">
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
            <div class="slider mt-3 " reverse="true" style="--width: 200px; --height: 200px; --quantity:8;">
                <div class="list">
                    <div class="item" style="--position: 1">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_1.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 2">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 3">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_3.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 4">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_4.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 5">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_5.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 6">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_6.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 7">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_5.png" alt="" class="rounded"/>
                    </div>
                    <div class="item" style="--position: 8">
                        <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_6.png" alt="" class="rounded"/>
                    </div>
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
        <script src="${pageContext.request.contextPath}/resources/userMain_js"></script>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>