<t:genericpage title="Header">
    <jsp:attribute name="head">
        <t:resources/>

        <script src="${pageContext.request.contextPath}/resources/userMain_css"></script>
        <script src="${pageContext.request.contextPath}/resources/userMain_js"></script>
        
        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>

    </jsp:attribute>

    <jsp:attribute name="header">
        
    </jsp:attribute>

    <jsp:attribute name="body">
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
                            <li class="nav-item">
                              <a class="nav-link active text-dark" href="#child1" data-bs-toggle="tab">Child1 of ALL</a>
                            </li>
                            <li class="nav-item">
                              <a class="nav-link text-dark" href="#child2" data-bs-toggle="tab">Child2 of ALL</a>
                            </li>
                            <li class="nav-item">
                              <a class="nav-link text-dark" href="#child3" data-bs-toggle="tab">Child3 of ALL</a>
                            </li>
                            <li class="nav-item">
                              <a class="nav-link text-dark" href="#child4" data-bs-toggle="tab">Child4 of ALL</a>
                            </li>
                        </div>
                        <div class="row">
                            <!-- Tab Content -->
                            <div class="col-8">
                                <div class="tab-content">
                                    <div class="tab-pane active" id="child1">
                                        <div class="row">
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Child 1</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 1 of child 1</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 2 of child 1</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 3 of child 1</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Child 2</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 1 of child 2</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 2 of child 2</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 3 of child 2</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Child 3</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 1 of child 3</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 2 of child 3</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 3 of child 3</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Child 4</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 1 of child 4</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 2 of child 4</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Chau 3 of child 4</a></li>
                                                </ul>
                                            </div>
                                            
                                        </div>
                                    </div>
                                
                                    <div class="tab-pane " id="child2">
                                        <div class="row">
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="tab-pane " id="child3">
                                        <div class="row">
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="tab-pane " id="child4">
                                        <div class="row">
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                            <div class="col">
                                                <a href="#" class="text-decoration-none text-dark blackLineUnderneath"><h5 class="mt-3">Thoi trang nam</h5></a>
                                                <ul class="list-unstyled">
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                    <li><a href="" class="text-decoration-none text-dark blackLineUnderneath">Shirt</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
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

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>