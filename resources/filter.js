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
    var url = new URL(
        "https://" + location.host + contextPath + "/ajax/variation"
    );

    if (categoryId) {
        url.searchParams.append("categoryId", categoryId);
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let variationContainer = document.getElementById("variationFilter");
            variationContainer.innerHTML = "";

            let ul = document.createElement("ul");
            data.forEach(variation => {
                ul.appendChild(createVariationElement(variation));
            });

            variationContainer.appendChild(ul);
        })
        .catch(error => console.error("Error fetching variations:", error));
}

function createVariationElement(variation) {
    let li = document.createElement("li");
    let label = document.createElement("label");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "variation";
    radio.value = variation.id;
    radio.dataset.name = variation.name;

    radio.addEventListener("change", function () {
        document.getElementById("selectedVariationId").value = variation.id;
        fetchVariationValues(variation.id);
    });

    label.appendChild(radio);
    label.appendChild(document.createTextNode(" " + variation.name));
    li.appendChild(label);

    return li;
}

function fetchVariationValues(variationId) {
    var url = new URL(
        "https://" + location.host + contextPath + "/ajax/variation"
    );

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

            let ul = document.createElement("ul");

            // Fix: Ensure we find the correct variation
            if (data.length > 0) {
                data.forEach(value => {
                    ul.appendChild(createVariationValueElement(value));
                });
            } else {
                console.error("Không tìm thấy values cho variationId:", variationId, "Dữ liệu API:", data);
            }

            variationValueContainer.appendChild(ul);
        })
        .catch(error => console.error("Lỗi khi lấy giá trị biến thể:", error));

}

function createVariationValueElement(value) {
    let li = document.createElement("li");
    let label = document.createElement("label");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "variationValue";
    radio.value = value.id;
    radio.dataset.name = value.value;
    radio.dataset.parent = value.variationId;

    label.appendChild(radio);
    label.appendChild(document.createTextNode(" " + value.value));
    li.appendChild(label);

    return li;
}

var selectedVariations = [];

function applyVariation() {
    let selectedVariation = document.querySelector("input[name='variation']:checked");
    if (!selectedVariation) {
        alert("Please select a variation.");
        return;
    }

    let variationId = selectedVariation.value; // Lấy ID
    let variationName = selectedVariation.dataset.name; // Lấy tên từ `data-name`
    let datatype = selectedVariation.dataset.datatype || "N/A"; // Lấy kiểu dữ liệu từ `data-datatype`
    let unit = selectedVariation.dataset.unit || "N/A"; // Lấy đơn vị từ `data-unit`

    let selectedValues = Array.from(document.querySelectorAll(`input[name="variationValue"]:checked`))
        .map(value => value.value);

    if (selectedValues.length === 0) {
        alert("Please select at least one variation value.");
        return;
    }

    // Kiểm tra tránh thêm trùng variation
    if (selectedVariations.some(v => v.variationId === variationId)) {
        alert("This variation has already been selected!");
        return;
    }

    selectedVariations.push({ variationId, variationName, datatype, unit, values: selectedValues });
    renderVariationTable();
}



function addNewVariation() {
    let newVariationName = document.getElementById("variationName").value.trim();
    let newVariationValues = document.getElementById("variationValues").value.trim().split(",");
    let newVariationDatatype = document.getElementById("datatype").value.trim();
    let newVariationUnit = document.getElementById("unit").value.trim();

    if (!newVariationName || !newVariationDatatype || !newVariationUnit || newVariationValues.length === 0) {
        alert("Please fill in all fields.");
        return;
    }

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

var selectedVariations = [];

function updateSelectedVariations() {
    selectedVariations = []; // Reset danh sách mỗi khi cập nhật

    let variationId = document.querySelector('input[name="variation"]:checked');
    if (!variationId) {
        console.error("Chưa chọn variation!");
        return;
    }

    let selectedValues = [];
    document.querySelectorAll('input[name="variationValue"]:checked').forEach(checkbox => {
        selectedValues.push(checkbox.value);
    });

    if (selectedValues.length === 0) {
        console.error("Chưa chọn giá trị variation!");
        return;
    }

    selectedVariations.push({
        variationId: variationId.value,
        values: selectedValues
    });

    console.log("Danh sách selectedVariations:", selectedVariations);
}

function submitVariations() {
    let form = document.getElementById("selectVariationForm");

    // Xóa các input ẩn cũ trước khi thêm mới
    document.querySelectorAll(".dynamic-input").forEach(e => e.remove());

    let formData = new FormData(form);

    selectedVariations.forEach(variation => {
        formData.append("variation", variation.variationId); // Gửi variation ID
        formData.append("datatype", variation.datatype); // Gửi datatype
        formData.append("unit", variation.unit); // Gửi unit

        variation.values.forEach(value => {
            formData.append("variationValue", value); // Gửi từng variationValue
        });
    });

    // Debug: Kiểm tra dữ liệu trước khi gửi
    for (let [key, value] of formData.entries()) {
        console.log(`Debug - ${key}: ${value}`);
    }

    fetch(form.action, {
        method: "POST",
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        console.log("Server response:", data);
        alert("Variations saved successfully!");
    })
    .catch(error => console.error("Error:", error));
}




