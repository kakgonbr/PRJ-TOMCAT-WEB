<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Add Product">
    <jsp:attribute name="head">
        <t:resources/>
        <script>
            var contextPath = "${pageContext.request.contextPath}";

            function createCategoryOptions(categories) {
                let categorySelect = document.getElementById("category");
                categorySelect.innerHTML = "";
                categories.forEach(category => {
                    let option = document.createElement("option");
                    option.value = category.id;
                    option.textContent = category.name;
                    categorySelect.appendChild(option);
                });
            }

            function fetchCategory() {
                fetch(contextPath + "/ajax/category")
                    .then(response => response.json())
                    .then(data => {
                        createCategoryOptions(data);
                    })
                    .catch(error => console.error("Error fetching categories:", error));
            }

            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
            });
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="addProduct"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Add Product</h2>
        <form id="addProductForm" action="${pageContext.request.contextPath}/addproduct" method="post">
            <input type="hidden" name="action" value="addProduct">

            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="name" required><br>

            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea><br>

            <label for="category">Category:</label>
            <select id="category" name="categoryId"></select><br>

            <button type="submit">Add Product</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>













        
        
        
        
        


<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Add Product & Select Variations">
    <jsp:attribute name="head">
        <t:resources/>
        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var variations = [];

            function createCategoryOptions(categories, parentElement, level = 0) {
                categories.forEach(category => {
                    let option = document.createElement("option");
                    option.value = category.id;
                    option.textContent = "-".repeat(level) + " " + category.name;
                    parentElement.appendChild(option);

                    if (category.children && category.children.length > 0) {
                        createCategoryOptions(category.children, parentElement, level + 1);
                    }
                });
            }

            function fetchCategory() {
                fetch(contextPath + "/ajax/category")
                    .then(response => response.json())
                    .then(data => {
                        let categorySelect = document.getElementById("category");
                        categorySelect.innerHTML = "";
                        createCategoryOptions([data], categorySelect);
                    })
                    .catch(error => console.error("Error fetching categories:", error));
            }

            function showStep(step) {
                document.getElementById("step1").style.display = (step === 1) ? "block" : "none";
                document.getElementById("step2").style.display = (step === 2) ? "block" : "none";
            }

            function nextStep() {
                showStep(2);
            }

            function addVariation() {
                let variationName = document.getElementById("variationName").value.trim();
                let variationOptions = document.getElementById("variationOptions").value.split(",").map(opt => opt.trim());
                let datatype = document.getElementById("datatype").value;
                let unit = document.getElementById("unit").value.trim();

                if (!variationName || variationOptions.length === 0 || !unit) {
                    alert("Please enter valid variation details.");
                    return;
                }

                let variation = { variationName, variationOptions, datatype, unit };
                variations.push(variation);

                updateVariationTable();
            }

            function updateVariationTable() {
                let table = document.getElementById("variationBody");
                table.innerHTML = "";

                variations.forEach((variation, index) => {
                    let row = table.insertRow();
                    row.insertCell(0).innerText = variation.variationName;
                    row.insertCell(1).innerText = variation.variationOptions.join(", ");
                    row.insertCell(2).innerText = variation.datatype;
                    row.insertCell(3).innerText = variation.unit;

                    let deleteCell = row.insertCell(4);
                    let deleteBtn = document.createElement("button");
                    deleteBtn.innerText = "Remove";
                    deleteBtn.onclick = function () {
                        removeVariation(index);
                    };
                    deleteCell.appendChild(deleteBtn);
                });
            }

            function removeVariation(index) {
                variations.splice(index, 1);
                updateVariationTable();
            }

            function submitVariations() {
                let form = document.getElementById("selectVariationForm");
                let input = document.createElement("input");
                input.type = "hidden";
                input.name = "variations";
                input.value = JSON.stringify(variations);
                form.appendChild(input);
                form.submit();
            }

            document.addEventListener("DOMContentLoaded", function () {
                fetchCategory();
                showStep(1);
            });
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="addProduct"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <!-- Step 1: Add Product -->
        <div id="step1">
            <h2>Add Product</h2>
            <form id="addProductForm">
                <input type="hidden" name="action" value="addProduct">

                <label for="productName">Product Name:</label>
                <input type="text" id="productName" name="productName" required><br>

                <label for="description">Description:</label>
                <textarea id="description" name="description"></textarea><br>

                <label for="category">Category:</label>
                <select id="category" name="category"></select><br>

<!--                <label for="price">Price:</label>
                <input type="number" id="price" name="price" required><br>

                <label for="stock">Stock:</label>
                <input type="number" id="stock" name="stock" required><br>-->

                <button type="button" onclick="nextStep()">Next</button>
            </form>
        </div>

        <!-- Step 2: Select Variations -->
        <div id="step2" style="display: none;">
            <h2>Select Variations</h2>
            <form id="selectVariationForm" action="${pageContext.request.contextPath}/addproduct" method="post" onsubmit="submitVariations(); return false;">
                <input type="hidden" name="action" value="selectVariation">

                <label for="variationName">Variation Name:</label>
                <input type="text" id="variationName" name="variationName"><br>

                <label for="variationOptions">Options (comma-separated):</label>
                <input type="text" id="variationOptions" name="variationOptions"><br>

                <label for="datatype">Data Type:</label>
                <select name="datatype" id="datatype">
                    <option value="string">String</option>
                    <option value="integer">Integer</option>
                </select><br>

                <label for="unit">Unit:</label>
                <input type="text" id="unit" name="unit"><br>

                <button type="button" onclick="addVariation()">Add Variation</button><br>

                <table border="1">
                    <thead>
                        <tr>
                            <th>Variation Name</th>
                            <th>Options</th>
                            <th>Data Type</th>
                            <th>Unit</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody id="variationBody"></tbody>
                </table>

                <button type="submit">Save Variations</button>
            </form>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>--%>
