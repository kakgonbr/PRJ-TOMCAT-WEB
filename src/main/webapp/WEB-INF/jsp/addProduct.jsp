<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        <script>
            var contextPath = "${pageContext.request.contextPath}";

            function fetchCategory() {
                fetch(contextPath + "/ajax/category")
                    .then(response => response.json())
                    .then(data => {
                        let categorySelect = document.getElementById("category");
                        categorySelect.innerHTML = "";
                        data.forEach(category => {
                            let option = document.createElement("option");
                            option.value = category.id;
                            option.textContent = category.name;
                            categorySelect.appendChild(option);
                        });
                    })
                    .catch(error => console.error("Error fetching categories:", error));
            }

            document.addEventListener("DOMContentLoaded", fetchCategory);
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="addProduct"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Add Product</h2>
        <form id="addProductForm" action="${pageContext.request.contextPath}/shop-management" method="post">
            <input type="hidden" name="action" value="addProduct">

            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="productName" required><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea><br>

            <label for="category">Category:</label>
            <select id="category" name="category"></select><br>

            <label for="price">Price:</label>
            <input type="number" id="price" name="price" required><br>

            <label for="stock">Stock:</label>
            <input type="number" id="stock" name="stock" required><br>

            <label for="image">Product Image:</label>
            <input type="file" id="image" name="image"><br>

            <button type="submit">Next</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
