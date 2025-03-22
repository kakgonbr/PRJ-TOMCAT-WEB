<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Catalog">
    <jsp:attribute name="head">
        <t:resources/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/catalog_css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/userMain_css">

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var categoryId = "${categoryId}"
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        <div class="container" >
            <!--app bar-->
            <div class="row my-2">
                <!--seller center-->
                <div class="col-2 d-flex align-items-center">
                    <a href="#" class="text-decoration-none text-dark fs-5">
                        <i class="bi bi-shop"></i>
                        <span class="text-muted"> Seller center</span>
                    </a>
                </div>
                <!--logo-->
                <div class="col-5 d-flex align-items-center justify-content-end ">
                    <p class="fs-2 fw-bold">BMMarket</p>
                </div>
                <!--menu-->
                <div class="col-5 d-flex align-items-center justify-content-end">
                    <!--notification-->
                    <div class="ms-4">
                        <a href="#" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                            <i class="bi bi-bell-fill"></i>
                            <span class="text-muted">Notification</span>
                        </a>
                    </div>
                    <!--chat-->
                    <div class="ms-4">
                        <a href="#" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                            <i class="bi bi-chat-fill"></i>
                            <span class="text-muted">Message</span>
                        </a>
                    </div>
                    <!--user-->
                    <div class="ms-4">
                        <a href="#" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                            <i class="bi bi-person-fill "></i>
                            <span class="text-muted">User</span>
                        </a>
                    </div>
                    <!--cart-->
                    <div class="ms-4">
                        <a href="#" class="p-2 text-decoration-none fs-5 logo rounded-pill">
                            <i class="bi bi-bag-fill"></i>
                            <span class="text-muted">Cart</span>
                        </a>
                    </div>
                </div>
            </div>
            <!--nav-->
            <div class="row" style="position: relative;">
                <div class="col-2 fs-5 fw-semibold dropdown dropdown-full-width" data-bs-toggle="dropdown" aria-expanded="false" type="button" id="category">
                    <i class="bi bi-list"></i>
                    <span >CATEGORY</span>
                </div>
                <div class="dropdown-menu border-0 shadow p-3 overflow-auto my-3 dropdown-menu-hover" >
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
                    <form class="input-group ms-2 rounded y w-75" style="background-color: rgb(248, 246, 246);">
                        <input type="text" class="form-control border-0 rounded" placeholder="Search for items and brands" aria-label="Search" data-bs-toggle="dropdown" aria-expanded="false" style="background-color: rgb(248, 246, 246);" name="searchInput" id="searchBar" autocomplete="off">
                        <input type="hidden" name="categoryId">
                        <button class="btn  border-0 rounded clear-btn">
                            <i class="bi bi-x-circle-fill"></i>
                        </button>
                        <button class="btn border-0 rounded-5 noHoverEffect" type="submit">
                            <i class="bi bi-search"></i> 
                        </button>
                        
                        <ul class="dropdown-menu border-0 shadow p-3 overflow-auto dropdown-menu-fullwidth" >
                            <li><a class="dropdown-item" href="#">lore</a></li>
                            <li><a class="dropdown-item" href="#">Another action</a></li>
                            <li><a class="dropdown-item" href="#">Something else here</a></li>
                        </ul>
                    </form>
                    
                </div>
                
            </div>
        </div>
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
        <script src="${pageContext.request.contextPath}/resources/catalog_js"></script>
        <script src="${pageContext.request.contextPath}/resources/userMain_js"></script>
    </jsp:attribute>
</t:genericpage>