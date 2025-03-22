<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Catalog">
    <jsp:attribute name="head">
        <t:resources/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/searchPage_css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/userMain_css">

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var categoryId = "${categoryId}"
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <main class="container my-5">
            <div class="d-flex justify-content-between">
                <h3 class="mb-4 fw-semibold d-flex">
                    <i class="bi bi-filter"></i>
                    <span>Filter</span>
                </h3>
                <div class="d-flex justify-content-end align-items-center">
                    <p class=" fw-bold mx-2">Sort by: </p>
                    <div class="dropdown-center sort-menu">
                        <p type="button" data-bs-toggle="dropdown" aria-expanded="false" class="text-muted sort-menu-chosen">Default<i class="bi bi-chevron-down"></i> </p>
                        <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#" >Popularity <i class="bi bi-check2"></i> </a></li>
                        <li><a class="dropdown-item" href="#">Price: Low to High <i class="bi bi-check2"></i> </a></li>
                        <li><a class="dropdown-item" href="#">Price: High to Low <i class="bi bi-check2"></i> </a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="row">
                <!-- Sidebar Filters -->
                <div class="col-3 mb-4">
                    <form class="sidebar">
                        <div class="filter-section mb-3">
                            <div class="collapse-header" data-bs-toggle="collapse" data-bs-target="#categoriesCollapse">
                                Categories <span class="float-end"><i class="bi bi-chevron-down text-dark"></i></span>
                            </div>
                            <div class="collapse show mt-2" id="categoriesCollapse" style="max-height: 250px; overflow: auto;">
                                <div class="collapse-content" id="categoriesCollapseContent">
                                    
                                </div>
                            </div>
                        </div>
    
                        <!-- Price Range -->
                        <div class="filter-section mb-3">
                            <div class="collapse-header" data-bs-toggle="collapse" data-bs-target="#priceRangeCollapse">
                                Price range <span class="float-end"><i class="bi bi-chevron-down text-dark"></i></span>
                            </div>
                            <div class="collapse show" id="priceRangeCollapse" >
                                <div class="collapse-content" style="position: relative;">
                                    <div class="group my-4">
                                        <div class="progress"></div>
                                        <div class="range-input">
                                            <input type="range" class="range-min" min="0" max="10000" step="100" value="0">
                                            <input type="range" class="range-max" min="0" max="10000" value="10000" step="100">
                                        </div>
                                    </div>
                                    <div class="my-3 d-flex align-items-center range-text ">
                                        <div class="d-flex align-items-center">
                                            <span class="mx-2">Min</span>
                                            <input type="text" class="form-control input-min" value="0" name="minPrice" >
                                        </div>
                                        <div class="d-flex align-items-center" >
                                            <span class="mx-2">Max</span>
                                            <input type="text" class="form-control input-max" value="10000" name="maxPrice">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
    
                        <!-- Noi ban : loc may thang filter hien tai cong them dia diem -->
                        <div class="filter-section mb-3">
                            <div class="collapse-header" data-bs-toggle="collapse" data-bs-target="#addressCollapse">
                                Noi ban <span class="float-end"><i class="bi bi-chevron-down text-dark"></i></span>
                            </div>
                            <div class="collapse show" id="addressCollapse">
                                <div class="collapse-content">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="city1" value="Ho Chi Minh" name="addressCity">
                                        <label class="form-check-label" for="city1">TP Ho Chi Minh</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="city2" value="Da Nang" name="addressCity">
                                        <label class="form-check-label" for="city2">Da Nang</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="city3" value="Ha Noi" name="addressCity">
                                        <label class="form-check-label" for="city3">Ha Noi</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="text-center">
                            <button class="btn btn-dark" type="submit">Apply</button>
                        </div>
                    </form>
                </div>
    
                <!-- Products Grid (unchanged from previous example) -->
                <div class="col-9 row">
                    <div class="col-3 mb-2">
                        <a href="#" class="text-dark text-decoration-none">
                        <div class="card">
                            <!--should have a id of product-->
                            <div class="position-relative ">
                                <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" class="card-img-top product-img " alt="" loading="lazy">
                            </div>
                            <div class="card-body d-flex flex-column justify-content-between ">
                                <p class="card-title mt-3 fw-semibold blackLineUnderneath">Lorem, ipsum dolor sit amet consect</p>
                                <!--take the rate and put it in a loop. if not even, the last start will be half -> if else -->
                                
                                <p class="card-text "><strong>$126.50</strong> <s class="text-muted">$165.00</s></p>
                            </div>
                        </div>
                        </a>
                    </div>
                    <div class="col-3 mb-2">
                        <a href="#" class="text-dark text-decoration-none">
                        <div class="card">
                            <!--should have a id of product-->
                            <div class="position-relative ">
                                <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" class="card-img-top product-img " alt="" loading="lazy">
                            </div>
                            <div class="card-body d-flex flex-column justify-content-between ">
                                <p class="card-title mt-3 fw-semibold blackLineUnderneath">Lorem, ipsum dolor sit amet consect</p>
                                <!--take the rate and put it in a loop. if not even, the last start will be half -> if else -->
                                
                                <p class="card-text "><strong>$126.50</strong> <s class="text-muted">$165.00</s></p>
                            </div>
                        </div>
                        </a>
                    </div>
                    <div class="col-3 mb-2">
                        <a href="#" class="text-dark text-decoration-none">
                        <div class="card">
                            <!--should have a id of product-->
                            <div class="position-relative ">
                                <img src="https://raw.githubusercontent.com/HoanghoDev/youtube_v2/main/auto_slider/images/slider2_2.png" class="card-img-top product-img " alt="" loading="lazy">
                            </div>
                            <div class="card-body d-flex flex-column justify-content-between ">
                                <p class="card-title mt-3 fw-semibold blackLineUnderneath">Lorem, ipsum dolor sit amet consect</p>
                                <!--take the rate and put it in a loop. if not even, the last start will be half -> if else -->
                                
                                <p class="card-text "><strong>$126.50</strong> <s class="text-muted">$165.00</s></p>
                            </div>
                        </div>
                        </a>
                    </div>
                    
                    
                    
                    <!--page-->
                    <div class="col-12 my-4 d-flex justify-content-center">
                        <nav aria-label="Page navigation example">
                            <ul class="pagination">
                                <!--depend on current page to calculate to page num-->
                                <li class="page-item"><a class="page-link text-dark" href="#">Previous</a></li>
                                <li class="page-item"><a class="page-link text-dark" href="#">1</a></li>
                                <li class="page-item"><a class="page-link text-dark" href="#">2</a></li>
                                <li class="page-item"><a class="page-link text-dark" href="#">3</a></li>
                                <li class="page-item"><a class="page-link text-dark" href="#">4</a></li>
                                <li class="page-item"><a class="page-link text-dark" href="#">5</a></li>
                              <li class="page-item"><a class="page-link text-dark" href="#">Next</a></li>
                            </ul>
                          </nav>
                    </div>
                </div>

            </div>
        </main>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
        <script src="${pageContext.request.contextPath}/resources/searchPage_js"></script>
        <script src="${pageContext.request.contextPath}/resources/userMain_js"></script>
    </jsp:attribute>
</t:genericpage>