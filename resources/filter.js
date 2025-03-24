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
  radio.dataset.datatype = variation.datatype || "N/A";
  radio.dataset.unit = variation.unit || "N/A";

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
  let checkbox = document.createElement("input");

  checkbox.type = "checkbox";  // Cho phép chọn nhiều giá trị
  checkbox.name = "variationValue";
  checkbox.value = value.id;
  checkbox.dataset.name = value.value;
  checkbox.dataset.parent = value.variationId;

  label.appendChild(checkbox);
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
  let variationDatatype = selectedVariation.dataset.datatype || "N/A";
  let variationUnit = selectedVariation.dataset.unit || "N/A";
  let selectedValues = Array.from(document.querySelectorAll(`input[name="variationValue"]:checked`))
    .map(value => value.dataset.name);

  if (selectedValues.length === 0) {
    alert("Please select at least one variation value.");
    return;
  }

  selectedVariations.push({
    variationName,
    values: selectedValues,
    datatype: variationDatatype,
    unit: variationUnit
  });
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
}

function submitVariations() {

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

  // Xóa các input ẩn cũ trước khi thêm mới
  document.querySelectorAll(".dynamic-input").forEach(e => e.remove());

  selectedVariations.forEach(variation => {
    let nameInput = document.createElement("input");
    nameInput.type = "hidden";
    nameInput.name = "variation";
    nameInput.value = variation.variationName;
    nameInput.classList.add("dynamic-input");
    form.appendChild(nameInput);

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
      let variationValues = variation.values.join(",");
      let valueInput = document.createElement("input");
      valueInput.type = "hidden";
      valueInput.name = "variationValue"; 
      valueInput.value = variationValues;
      valueInput.classList.add("dynamic-input");
      form.appendChild(valueInput);
    });
  });

  console.log("Dữ liệu gửi đi:", selectedVariations);
  form.submit();
}



