<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Product">
  <jsp:attribute name="head">
    <t:resources />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/productInfoTest_css">
    <script>
      var contextPath = "${pageContext.request.contextPath}";
      var productId = ${param.productId};
    </script>
  </jsp:attribute>

  <jsp:attribute name="header">
      <t:userHeader user="${sessionScope.user.username}" />
  </jsp:attribute>

  <jsp:attribute name="body">
    <main class="container my-5">
      <div class="row ">
        <div class="col-6 row">
          <div class="col-12 rounded mx-auto mb-2" style="max-width: 620px;">
            <img id="mainImage" class="rounded"
              src=""
              alt="Product Image" style="width: 100%; object-fit: cover; height: 600px">
          </div>
          <div class="d-flex align-items-center overflow-hidden" style="position: relative;">
            <button class="slider-btn left" onclick="moveSlider(-1)"><i class="bi bi-chevron-left"></i></button>
            <div class="slider rounded" id="product-images-container">
              <%-- <img
                src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_1.png"
                class="img-slider rounded" onmouseover="changeImage(this)">
              <img
                src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png"
                class="img-slider rounded" onmouseover="changeImage(this)">
              <img
                src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_3.png"
                class="img-slider rounded" onmouseover="changeImage(this)">
              <img
                src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_4.png"
                class="img-slider rounded" onmouseover="changeImage(this)">
              <img
                src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_5.png"
                class="img-slider rounded" onmouseover="changeImage(this)">
              <img
                src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_6.png"
                class="img-slider rounded" onmouseover="changeImage(this)"> --%>
            </div>
            <button class="slider-btn right" onclick="moveSlider(1)"><i class="bi bi-chevron-right"></i></button>
          </div>
        </div>
        <div class="col-6 row">
          <h1 class="my-3" id="product-name">Loading</h1>
          <h3 class="my-3" id="product-shop">Loading</h2>
          <h3 class="my-3" id="product-category">Loading</h2>
          <div id="promotion-container">
          
          </div>
          <p class="text-start" id="product-desc">Loading</p>

          <div class="my-3" id="customizations">

          </div>
          <h4>Stock: </h4>
          <h4 id="stock-counter">Loading</h4>
          <h4>Price: </h4>
          <h4 id="price-counter">Loading</h4>
          <form action="${pageContext.request.contextPath}/cart" method="POST">
            <input type="hidden" id="productItemId" name="productItemId">
            <div class="quantity-container">
              <div class="quantity-btn" onclick="updateQuantity(-1)"><i class="bi bi-dash-lg"></i></div>
              <input type="text" class="quantity-input" id="quantity" name="quantity" value="1" min="1"
              autocomplete="off">
              <div class="quantity-btn" onclick="updateQuantity(1)"><i class="bi bi-plus-lg"></i></div>
            </div>
            <button id="add-to-cart" class="cartBtn" type="submit">
              <svg class="cart" fill="white" viewBox="0 0 576 512" height="1em"
                xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M0 24C0 10.7 10.7 0 24 0H69.5c22 0 41.5 12.8 50.6 32h411c26.3 0 45.5 25 38.6 50.4l-41 152.3c-8.5 31.4-37 53.3-69.5 53.3H170.7l5.4 28.5c2.2 11.3 12.1 19.5 23.6 19.5H488c13.3 0 24 10.7 24 24s-10.7 24-24 24H199.7c-34.6 0-64.3-24.6-70.7-58.5L77.4 54.5c-.7-3.8-4-6.5-7.9-6.5H24C10.7 48 0 37.3 0 24zM128 464a48 48 0 1 1 96 0 48 48 0 1 1 -96 0zm336-48a48 48 0 1 1 0 96 48 48 0 1 1 0-96z">
                </path>
              </svg>
              ADD TO CART
              <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 640 512" class="pd-icon">
                <path
                  d="M211.8 0c7.8 0 14.3 5.7 16.7 13.2C240.8 51.9 277.1 80 320 80s79.2-28.1 91.5-66.8C413.9 5.7 420.4 0 428.2 0h12.6c22.5 0 44.2 7.9 61.5 22.3L628.5 127.4c6.6 5.5 10.7 13.5 11.4 22.1s-2.1 17.1-7.8 23.6l-56 64c-11.4 13.1-31.2 14.6-44.6 3.5L480 197.7V448c0 35.3-28.7 64-64 64H224c-35.3 0-64-28.7-64-64V197.7l-51.5 42.9c-13.3 11.1-33.1 9.6-44.6-3.5l-56-64c-5.7-6.5-8.5-15-7.8-23.6s4.8-16.6 11.4-22.1L137.7 22.3C155 7.9 176.7 0 199.2 0h12.6z">
                </path>
              </svg>
            </button>
          </form>
        </div>
        
      </div>
      <div class="row">

      </div>
      <div class="row">
        <div class="col-12">
            <div id="average-rating-container" class="my-4">
                <h3>Average Rating: <span id="average-rating">0.0</span>/5.0 ★</h3>
            </div>

            <c:if test="${sessionScope.user != null}">
              <div class="review-form-container mb-4">
                <h3>Write a Review</h3>
                <form id="reviewForm" class="review-form">
                    <div class="rating-input mb-3">
                        <label>Rating:</label>
                        <div class="star-rating">
                            <input type="radio" id="star5" name="rate" value="5">
                            <label for="star5">★</label>
                            <input type="radio" id="star4" name="rate" value="4">
                            <label for="star4">★</label>
                            <input type="radio" id="star3" name="rate" value="3">
                            <label for="star3">★</label>
                            <input type="radio" id="star2" name="rate" value="2">
                            <label for="star2">★</label>
                            <input type="radio" id="star1" name="rate" value="1">
                            <label for="star1">★</label>
                        </div>
                    </div>
                    <div class="form-group mb-3">
                        <label for="comment">Comment:</label>
                        <textarea class="form-control" id="comment" name="comment" rows="3" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit Review</button>
                </form>
              </div>
            </c:if>
            
            <h2 class="my-4">Reviews: </h2>
            <div id="reviews-container">
                <!-- Reviews will be added here -->
            </div>
        </div>
      </div>
    </main>
  </jsp:attribute>

  <jsp:attribute name="footer">
    <t:footer />
    <script src="${pageContext.request.contextPath}/resources/productInfoTest_js"></script>
  </jsp:attribute>
</t:genericpage>