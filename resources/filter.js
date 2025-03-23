function fetchCategory() {
    fetch(contextPath + "/ajax/category")
        .then(response => response.json())
        .then(data => {
            let treeContainer = document.getElementById("categoryFilter");
            treeContainer.innerHTML = "";
            treeContainer.appendChild(createCategoryElement(data));
        })
        .catch(error => console.error("Error fetching filters:", error));
}

function createCategoryElement(category) {
    let li = document.createElement("li");
    let label = document.createElement("label");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "filter";
    radio.value = category.id;

    label.appendChild(radio);
    label.appendChild(document.createTextNode(" " + category.name));
    li.appendChild(label);

    if (category.children && category.children.length > 0) {
        let ul = document.createElement("ul");
        category.children.forEach(child => {
            ul.appendChild(createCategoryElement(child));
        });
        li.appendChild(ul);
    }
    return li;
}

function fetchVariations(categoryId) {
    var url = new URL("https://" + location.host + contextPath + "/ajax/variation");

    if (categoryId) {
        url.searchParams.append("categoryId", categoryId);
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let variationContainer = document.getElementById("variationFilter");
            variationContainer.innerHTML = "";

            let table = document.createElement("table");
            table.className = "table table-striped table-bordered";

            let thead = document.createElement("thead");
            thead.innerHTML = `
                <tr class="table-dark">
                    <th>Select</th>
                    <th>Variation Name</th>
                    <th>Data Type</th>
                    <th>Unit</th>
                </tr>
            `;
            table.appendChild(thead);

            // Tạo phần thân bảng
            let tbody = document.createElement("tbody");

            data.forEach(variation => {
                tbody.appendChild(createVariationElement(variation));
            });

            table.appendChild(tbody);
            variationContainer.appendChild(table);
        })
        .catch(error => console.error("Error fetching variations:", error));
}
function createVariationElement(variation) {
    let tr = document.createElement("tr");

    let selectTd = document.createElement("td");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "variation";
    radio.value = variation.id;
    radio.dataset.name = variation.name;

    radio.addEventListener("change", function () {
        fetchVariationValues(variation.id);
    });

    selectTd.appendChild(radio);
    tr.appendChild(selectTd);

    // Tên variation
    let nameTd = document.createElement("td");
    nameTd.textContent = variation.name;
    tr.appendChild(nameTd);

    // Datatype
    let dataTypeTd = document.createElement("td");
    dataTypeTd.textContent = variation.datatype || "N/A";
    tr.appendChild(dataTypeTd);

    // Unit
    let unitTd = document.createElement("td");
    unitTd.textContent = variation.unit || "N/A";
    tr.appendChild(unitTd);

    return tr;
}

function fetchVariationValues(variationId) {
    var url = new URL("https://" + location.host + contextPath + "/ajax/variation");

    if (variationId) {
        url.searchParams.append("variationId", variationId);
    }

    fetch(url.toString())
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Dữ liệu nhận được:", data);

            let variationValueContainer = document.getElementById("variationValueFilter");
            variationValueContainer.innerHTML = "";

            let table = document.createElement("table");
            table.className = "table table-striped table-bordered";

            let thead = document.createElement("thead");
            thead.innerHTML = `
                <tr class="table-dark">
                    <th>Select</th>
                    <th>Value Name</th>
                </tr>
            `;
            table.appendChild(thead);

            let tbody = document.createElement("tbody");

            if (data.length > 0) {
                data.forEach(value => {
                    tbody.appendChild(createVariationValueElement(value));
                });
            } else {
                console.error("Không tìm thấy values cho variationId:", variationId, "Dữ liệu API:", data);
            }

            table.appendChild(tbody);
            variationValueContainer.appendChild(table);
        })
        .catch(error => console.error("Lỗi khi lấy giá trị biến thể:", error));
}

function createVariationValueElement(value) {
    let tr = document.createElement("tr");

    let selectTd = document.createElement("td");
    let checkbox = document.createElement("input");
    checkbox.type = "checkbox"; // Cho phép chọn nhiều values
    checkbox.name = "variationValue";
    checkbox.value = value.id;
    checkbox.dataset.name = value.value;
    checkbox.dataset.parent = value.variationId;

    selectTd.appendChild(checkbox);
    tr.appendChild(selectTd);

    let nameTd = document.createElement("td");
    nameTd.textContent = value.value;
    tr.appendChild(nameTd);

    return tr;
}

var selectedVariations = [];

function applyVariation() {
    let selectedVariation = document.querySelector("input[name='variation']:checked");

    let variationId = selectedVariation.value;
    let variationName = selectedVariation.dataset.name;
    let selectedValues = Array.from(document.querySelectorAll(`input[name="variationValue"]:checked`))
        .map(value => value.dataset.name);


    selectedVariations.push({ variationName, values: selectedValues });
    renderVariationTable();
}

function showNewVariationForm() {
    document.getElementById("newVariationForm").style.display = "block";
}

function addNewVariation() {
    let newVariationName = document.getElementById("variationName").value.trim();
    let newVariationValues = document.getElementById("variationValues").value.trim().split(",");
    let newVariationDatatype = document.getElementById("datatype").value.trim();
    let newVariationUnit = document.getElementById("unit").value.trim();


    selectedVariations.push({
        variationName: newVariationName,
        datatype: newVariationDatatype,
        unit: newVariationUnit,
        values: newVariationValues
    });

    renderVariationTable();

    document.getElementById("variationName").value = "";
    document.getElementById("variationDatatype").value = "";
    document.getElementById("variationUnit").value = "";
    document.getElementById("variationValue").value = "";
    document.getElementById("newVariationForm").style.display = "none";
}

function renderVariationTable() {
    let tableBody = document.getElementById("variationTableBody");
    tableBody.innerHTML = "";

    selectedVariations.forEach((variation, index) => {
        let row = document.createElement("tr");

        let nameCell = document.createElement("td");
        nameCell.textContent = variation.variationName;

        let valuesCell = document.createElement("td");
        valuesCell.textContent = variation.values.join(", ");

        let datatypeCell = document.createElement("td");
        datatypeCell.textContent = variation.datatype || "N/A";

        let unitCell = document.createElement("td");
        unitCell.textContent = variation.unit || "N/A";


        let actionCell = document.createElement("td");
        let removeButton = document.createElement("button");
        removeButton.textContent = "Remove";
        removeButton.onclick = function () {
            selectedVariations.splice(index, 1);
            renderVariationTable();
        };
        actionCell.appendChild(removeButton);

        row.appendChild(nameCell);
        row.appendChild(valuesCell);
        row.appendChild(datatypeCell);
        row.appendChild(unitCell);
        row.appendChild(actionCell);
        tableBody.appendChild(row);
    });
}

function submitVariations() {
    let form = document.getElementById("selectVariationForm");
    document.querySelectorAll(".dynamic-input").forEach(e => e.remove());

    selectedVariations.forEach((variation, index) => {
        let variationNameInput = document.createElement("input");
        variationNameInput.type = "hidden";
        variationNameInput.name = "variation";
        variationNameInput.value = variation.variationName;
        variationNameInput.classList.add("dynamic-input");
        form.appendChild(variationNameInput);

        let datatypeInput = document.createElement("input");
        datatypeInput.type = "hidden";
        datatypeInput.name = "datatype";
        datatypeInput.value = variation.datatype;
        datatypeInput.classList.add("dynamic-input");
        form.appendChild(datatypeInput);

        let unitInput = document.createElement("input");
        unitInput.type = "hidden";
        unitInput.name = "unit";
        unitInput.value = variation.unit;
        unitInput.classList.add("dynamic-input");
        form.appendChild(unitInput);

        variation.values.forEach(value => {
            let valueInput = document.createElement("input");
            valueInput.type = "hidden";
            valueInput.name = "variationValue";
            valueInput.value = value;
            valueInput.classList.add("dynamic-input");
            form.appendChild(valueInput);
        });
    });
    document.querySelectorAll("input[name='variationValue']").forEach(input => {
        console.log("Hidden input:", input.value);
    });
    form.submit();
}




