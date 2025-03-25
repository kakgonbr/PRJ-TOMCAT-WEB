<%-- POSSIBLE REQUEST ATTRIBUTES: --%>
<%-- POSSIBLE REQUEST PARAMETERS: --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage title="Admin Control Panel">
    <jsp:attribute name="head">
        <t:resources />

        <script src="${pageContext.request.contextPath}/resources/cp_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            document.addEventListener("DOMContentLoaded", function () {
                fetchData();
            });
        </script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin_css">
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:error error="${error}" />
        <h1>Admin page.</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <%-- <div>
            <h2>Quick Actions</h2>
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="enableMaintenance" />
                <input type="submit" value="Enable Maintenance" />
            </form>
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="disableMaintenance" />
                <input type="submit" value="Disable Maintenance" />
            </form>
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="logStatistics" />
                <input type="submit" value="Log today's statistics" />
            </form>
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="calculateTFIDF" />
                <input type="submit" value="Calculate TFIDF" />
            </form>
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="cleanup" />
                <input type="submit" value="Clean up files" />
            </form>
            </div>

            <div class="table-container">
                <h2>Resource List</h2>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Path</th>
                        </tr>
                    </thead>
                    <tbody class="data-body" id="resources">

                    </tbody>
                </table>
                <a href="${pageContext.request.contextPath}/admin/cp?table=resources&action=edit">Create
                    a new resource mapping</a>
            </div>

            <div class="table-container">
                <h2>Product List</h2>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Category ID</th>
                            <th>Available Promotion ID</th>
                            <th>Image Resource ID</th>
                            <th>Shop ID</th>
                        </tr>
                    </thead>
                    <tbody class="data-body" id="products">

                    </tbody>
                </table>
                <a href="${pageContext.request.contextPath}/admin/cp?table=products&action=edit">Create
                    a new product</a>
            </div>

            <div class="table-container">
                <h2>User List</h2>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Email</th>
                            <th>Username</th>
                            <th>Phonenumber</th>
                            <th>Password</th>
                            <th>Persistent Cookie</th>
                            <th>Google ID</th>
                            <th>Facebook ID</th>
                            <th>Display Name</th>
                            <th>Bio</th>
                            <th>Is Admin</th>
                            <th>Credit</th>
                            <th>Profile String Resource ID</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody class="data-body" id="users">

                    </tbody>
                </table>
                <a href="${pageContext.request.contextPath}/admin/cp?table=users&action=edit">Create
                    a new user</a>
            </div>

            <div class="table-container">
                <h2>Shop List</h2>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Address</th>
                            <th>Is Visible</th>
                            <th>Owner ID</th>
                        </tr>
                    </thead>
                    <tbody class="data-body" id="shops">

                    </tbody>
                </table>
                <a href="${pageContext.request.contextPath}/admin/cp?table=shops&action=edit">Create
                    a new shop</a>
            </div>

            <div class="table-container">
                <h2>Promotion List</h2>
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Value</th>
                            <th>ExpireDate</th>
                            <th>Type</th>
                            <th>Is Of Admin</th>
                            <th>Creation Date</th>
                            <th>Creator ID</th>
                        </tr>
                    </thead>
                    <tbody class="data-body" id="promotions">

                    </tbody>
                </table>
                <a href="${pageContext.request.contextPath}/admin/cp?table=promotions&action=edit">Create
                    a new promotion</a>
            </div> --%>

            <div class="container">
                <div class="mb-4">
                    <h2>Quick Actions</h2>
                    <div class="d-flex gap-2 flex-wrap">
                        <form action="${pageContext.request.contextPath}/admin" method="POST">
                            <input type="hidden" name="action" value="enableMaintenance" />
                            <button type="submit" class="btn btn-warning">Enable
                                Maintenance</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin" method="POST">
                            <input type="hidden" name="action" value="disableMaintenance" />
                            <button type="submit" class="btn btn-success">Disable
                                Maintenance</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin" method="POST">
                            <input type="hidden" name="action" value="logStatistics" />
                            <button type="submit" class="btn btn-info">Log Today's
                                Statistics</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin" method="POST">
                            <input type="hidden" name="action" value="calculateTFIDF" />
                            <button type="submit" class="btn btn-primary">Calculate TFIDF</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin" method="POST">
                            <input type="hidden" name="action" value="cleanup" />
                            <button type="submit" class="btn btn-danger">Clean Up Files</button>
                        </form>
                    </div>
                </div>

                <div class="table-container">
                    <h2>Resource List</h2>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Path</th>
                            </tr>
                        </thead>
                        <tbody id="resources"></tbody>
                    </table>
                    <a class="btn btn-primary"
                        href="${pageContext.request.contextPath}/admin/cp?table=resources&action=edit">Create
                        a new resource mapping</a>
                </div>

                <div class="table-container">
                    <h2>Product List</h2>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Category ID</th>
                                <th>Available Promotion ID</th>
                                <th>Image Resource ID</th>
                                <th>Shop ID</th>
                            </tr>
                        </thead>
                        <tbody id="products"></tbody>
                    </table>
                    <a class="btn btn-primary"
                        href="${pageContext.request.contextPath}/admin/cp?table=products&action=edit">Create
                        a new product</a>
                </div>

                <div class="table-container">
                    <h2>User List</h2>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Email</th>
                                <th>Username</th>
                                <th>Phonenumber</th>
                                <th>Password</th>
                                <th>Persistent Cookie</th>
                                <th>Google ID</th>
                                <th>Facebook ID</th>
                                <th>Display Name</th>
                                <th>Bio</th>
                                <th>Is Admin</th>
                                <th>Credit</th>
                                <th>Profile String Resource ID</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody id="users"></tbody>
                    </table>
                    <a class="btn btn-primary"
                        href="${pageContext.request.contextPath}/admin/cp?table=users&action=edit">Create
                        a new user</a>
                </div>

                <div class="table-container">
                    <h2>Shop List</h2>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Address</th>
                                <th>Is Visible</th>
                                <th>Owner ID</th>
                            </tr>
                        </thead>
                        <tbody id="shops"></tbody>
                    </table>
                    <a class="btn btn-primary"
                        href="${pageContext.request.contextPath}/admin/cp?table=shops&action=edit">Create
                        a new shop</a>
                </div>

                <div class="table-container">
                    <h2>Promotion List</h2>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Value</th>
                                <th>Expire Date</th>
                                <th>Type</th>
                                <th>Is Of Admin</th>
                                <th>Creation Date</th>
                                <th>Creator ID</th>
                            </tr>
                        </thead>
                        <tbody id="promotions"></tbody>
                    </table>
                    <a class="btn btn-primary"
                        href="${pageContext.request.contextPath}/admin/cp?table=promotions&action=edit">Create
                        a new promotion</a>
                </div>
            </div>

    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer />
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:
    ${pageContext.request.contextPath}/admin/cp?table=products&action=edit&id=1--%>
    <%-- <c:forEach var="resource" items="${resources}" varStatus="status">
        <tr>
            <td>${resource.id}</td>
            <td>${resource.systemPath}</td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                    <input type="hidden" name="systemPath" value="${resource.systemPath}">
                    <input type="hidden" name="table" value="resources">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" value="${resource.id}">
                    <button type="submit">Edit</button>
                </form>
                <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                    <input type="hidden" name="table" value="resources">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${resource.id}">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        </c:forEach> --%>

        <%-- <table border="1">

            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Description</th>
                <th>CategoryId</th>
                <th>availablePromotionId</th>
                <th>imageStringResourceId</th>
                <th>shopId</th>
            </tr>
            <c:forEach var="product" items="${products}" varStatus="status">
                <c:if test="${status.index >= start && status.index < end}">
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.price}</td>
                        <td>${product.description}</td>
                        <td>${product.categoryId}</td>
                        <td>${product.availablePromotionId}</td>
                        <td>${product.imageStringResourceId}</td>
                        <td>${product.shopId}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                                <input type="hidden" name="table" value="products">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="id" value="${product.id}">
                                <button type="submit">Edit</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/admin/cp" method="get">
                                <input type="hidden" name="table" value="products">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${product.id}">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            </table> --%>