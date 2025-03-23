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
    let url = new URL(location.origin + contextPath + "/ajax/variation");
  
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
            "<p class='text-danger'>No variations found for this category.</p>";
          return;
        }
  
        let table = document.createElement("table");
        table.classList.add("table", "table-bordered", "table-hover");
  
        let thead = document.createElement("thead");
        thead.innerHTML = `
                  <tr class="table-dark">
                      <th class="text-center">Select</th>
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
    selectTd.classList.add("text-center");
  
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "variation";
    radio.value = variation.id;
    radio.classList.add("form-check-input");
    radio.dataset.name = variation.name;
    radio.dataset.datatype = variation.datatype || "N/A";
    radio.dataset.unit = variation.unit || "N/A";
  
    radio.addEventListener("change", function () {
      fetchVariationValues(variation.id);
    });
  
    selectTd.appendChild(radio);
    tr.appendChild(selectTd);
  
    tr.innerHTML += `
        <td>${variation.name}</td>
        <td>${variation.datatype || "N/A"}</td>
        <td>${variation.unit || "N/A"}</td>
    `;
  
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
      .then((response) => response.json())
      .then((data) => {
        let variationValueContainer = document.getElementById("variationValueFilter");
        variationValueContainer.innerHTML = "";
  
        let ul = document.createElement("ul");
        ul.classList.add("list-group");
  
        if (data.length > 0) {
          data.forEach((value) => {
            ul.appendChild(createVariationValueElement(value));
          });
        } else {
          variationValueContainer.innerHTML =
            "<p class='text-warning'>No values found for this variation.</p>";
        }
  
        variationValueContainer.appendChild(ul);
      })
      .catch((error) => console.error("Error fetching variation values:", error));
  }
  
  function createVariationValueElement(value) {
    let li = document.createElement("li");
    li.classList.add("list-group-item");
    let label = document.createElement("label");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "variationValue";
    radio.value = value.id;
    radio.dataset.name = value.value;
    radio.dataset.parent = value.variationId;
    radio.classList.add("form-check-input");
  
    label.appendChild(radio);
    label.appendChild(document.createTextNode(" " + value.value));
    li.appendChild(label);
  
    return li;
  }
  
  function renderVariationTable() {
    let tableBody = document.getElementById("variationTableBody");
    tableBody.innerHTML = "";
  
    selectedVariations.forEach((variation, index) => {
      let row = document.createElement("tr");
  
      row.innerHTML = `
        <td>${variation.variationName}</td>
        <td>${variation.values.join(", ")}</td>
        <td>${variation.datatype || "N/A"}</td>
        <td>${variation.unit || "N/A"}</td>
        <td class="text-center">
          <button class="btn btn-danger btn-sm" onclick="removeVariation(${index})">Remove</button>
        </td>
      `;
  
      tableBody.appendChild(row);
    });
  }
  
  function removeVariation(index) {
    selectedVariations.splice(index, 1);
    renderVariationTable();
  }
  
  function applyVariation() {
    let selectedVariation = document.querySelector("input[name='variation']:checked");
    if (!selectedVariation) {
      alert("Please select a variation.");
      return;
    }
  
    let variationId = selectedVariation.value;
    let variationName = selectedVariation.dataset.name;
    let variationDatatype = selectedVariation.dataset.datatype;
    let variationUnit = selectedVariation.dataset.unit;
    let selectedValues = Array.from(document.querySelectorAll(`input[name="variationValue"]:checked`))
      .map((value) => value.dataset.name);
  
    if (selectedValues.length === 0) {
      alert("Please select at least one variation value.");
      return;
    }
  
    selectedVariations.push({
      variationName,
      values: selectedValues,
      datatype: variationDatatype,
      unit: variationUnit,
    });
  
    renderVariationTable();
  }
  
  function submitVariations() { 
    let form = document.getElementById("selectVariationForm");

    // Xóa các input ẩn cũ trước khi thêm mới
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

    form.submit();
}