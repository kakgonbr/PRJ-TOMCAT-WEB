function fetchCategory() {
  fetch(contextPath + "/ajax/category")
    .then((response) => response.json())
    .then((data) => {
      let treeContainer = document.getElementById("categoryFilter");
      treeContainer.innerHTML = "";
      treeContainer.appendChild(createCategoryElement(data));
    })
    .catch((error) => console.error("Error fetching filters:", error));
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
    category.children.forEach((child) => {
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
    .then((response) => response.json())
    .then((data) => {
      let variationContainer = document.getElementById("variationFilter");
      variationContainer.innerHTML = "";

      if (data.length === 0) {
        variationContainer.innerHTML =
          "<p style='color: red;'>No variations found for this category.</p>";
        return;
      }

      let table = document.createElement("table");
      table.classList.add("variation-table"); // Apply CSS class

      let thead = document.createElement("thead");
      thead.innerHTML = `
                <tr>
                    <th>Select</th>
                    <th>Variation Name</th>
                    <th>Data Type</th>
                    <th>Unit</th>
                </tr>
            `;
      table.appendChild(thead);

      let tbody = document.createElement("tbody");
      data.forEach((variation) => {
        tbody.appendChild(createVariationElement(variation));
      });

      table.appendChild(tbody);
      variationContainer.appendChild(table);
    })
    .catch((error) => console.error("Error fetching variations:", error));
}

function createVariationElement(variation) {
  let tr = document.createElement("tr");

  let selectTd = document.createElement("td");
  let radio = document.createElement("input");
  radio.type = "radio";
  radio.name = "variation";
  radio.value = variation.id;
  radio.dataset.name = variation.name;
  radio.dataset.datatype = variation.datatype || "N/A";
  radio.dataset.unit = variation.unit || "N/A";

  radio.addEventListener("change", function () {
    fetchVariationValues(variation.id);
  });

  selectTd.appendChild(radio);
  tr.appendChild(selectTd);

  let nameTd = document.createElement("td");
  nameTd.textContent = variation.name;
  tr.appendChild(nameTd);

  let datatypeTd = document.createElement("td");
  datatypeTd.textContent = variation.datatype || "N/A";
  tr.appendChild(datatypeTd);

  let unitTd = document.createElement("td");
  unitTd.textContent = variation.unit || "N/A";
  tr.appendChild(unitTd);

  return tr;
}

function fetchVariationValues(variationId) {
  var url = new URL(
    "https://" + location.host + contextPath + "/ajax/variation"
  );

  if (variationId) {
    url.searchParams.append("variationId", variationId);
  }

  fetch(url.toString())
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then((data) => {
      console.log("Dữ liệu nhận được:", data);

      let variationValueContainer = document.getElementById(
        "variationValueFilter"
      );
      variationValueContainer.innerHTML = "";

      let ul = document.createElement("ul");

      // Fix: Ensure we find the correct variation
      if (data.length > 0) {
        data.forEach((value) => {
          ul.appendChild(createVariationValueElement(value));
        });
      } else {
        console.error(
          "Không tìm thấy values cho variationId:",
          variationId,
          "Dữ liệu API:",
          data
        );
      }

      variationValueContainer.appendChild(ul);
    })
    .catch((error) => console.error("Lỗi khi lấy giá trị biến thể:", error));
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
  let selectedVariation = document.querySelector(
    "input[name='variation']:checked"
  );
  if (!selectedVariation) {
    alert("Please select a variation.");
    return;
  }

  let variationId = selectedVariation.value;
  let variationName = selectedVariation.dataset.name;
  let selectedValues = Array.from(
    document.querySelectorAll(`input[name="variationValue"]:checked`)
  ).map((value) => value.dataset.name);

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
  let newVariationValues = document
    .getElementById("variationValues")
    .value.trim()
    .split(",");
  let newVariationDatatype = document.getElementById("datatype").value.trim();
  let newVariationUnit = document.getElementById("unit").value.trim();

  if (
    !newVariationName ||
    !newVariationDatatype ||
    !newVariationUnit ||
    newVariationValues.length === 0
  ) {
    alert("Please fill in all fields.");
    return;
  }

  selectedVariations.push({
    variationName: newVariationName,
    datatype: newVariationDatatype,
    unit: newVariationUnit,
    values: newVariationValues,
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
