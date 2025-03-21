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

function fetchVariation() {
    fetch(contextPath + "/ajax/variation")
        .then(response => response.json())
        .then(data => {
            let treeContainer = document.getElementById("variationFilter");
            treeContainer.innerHTML = "";
            treeContainer.appendChild(createVariationElement(data));
        })
        .catch(error => console.error("Error fetching variations:", error));
}

function createVariationElement(variation) {
    let li = document.createElement("li");
    let label = document.createElement("label");

    // Tạo checkbox cho Variation
    let checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.name = "variation";
    checkbox.value = variation.id;

    label.appendChild(checkbox);
    label.appendChild(document.createTextNode(" " + variation.name));
    li.appendChild(label);

    // Nếu có danh sách VariationValue (con), thêm vào
    if (variation.variationValues && variation.variationValues.length > 0) {
        let ul = document.createElement("ul");
        ul.style.display = "none"; // Ẩn danh sách con ban đầu

        variation.variationValues.forEach(value => {
            let valueLi = document.createElement("li");

            let valueLabel = document.createElement("label");
            let valueCheckbox = document.createElement("input");
            valueCheckbox.type = "checkbox";
            valueCheckbox.name = "variationValue";
            valueCheckbox.value = value.id;

            valueLabel.appendChild(valueCheckbox);
            valueLabel.appendChild(document.createTextNode(" " + value.value));
            valueLi.appendChild(valueLabel);

            ul.appendChild(valueLi);
        });
        li.appendChild(ul);

        // Khi chọn Variation, hiển thị danh sách VariationValue
        checkbox.addEventListener("change", function () {
            ul.style.display = checkbox.checked ? "block" : "none";
        });
    }

    return li;
}
