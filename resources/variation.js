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

    let variationId = selectedVariation.value;
    let variationName = selectedVariation.dataset.name;
    let selectedValues = Array.from(document.querySelectorAll(`input[name="variationValue"]:checked`))
        .map(value => value.dataset.name);

    if (selectedValues.length === 0) {
        alert("Please select at least one variation value.");
        return;
    }

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
    let newVariationUnit = document.getElementById("uunit").value.trim();

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

function submitVariations() {
    let form = document.getElementById("selectVariationForm");
    let hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "selectedVariations";
    hiddenInput.value = JSON.stringify(selectedVariations);
    form.appendChild(hiddenInput);
    form.submit();
}