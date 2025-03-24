function onSelectCategory() {
    filter = document.querySelector('input[name="filter"]:checked');
    
    fetchVariations(filter ? filter.value : 0);
}

var variations = [];

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
            variations = data;

            let variationContainer = document.getElementById("variation-container");
            variationContainer.innerHTML = "";

            data.forEach(variation => {
                let li = document.createElement("li");
                let label = document.createElement("label");
                let checkbox = document.createElement("input");

                checkbox.type = "checkbox";
                checkbox.name = "variation";
                checkbox.value = variation.id;

                label.appendChild(checkbox);
                label.appendChild(document.createTextNode(" " + variation.name));
                li.appendChild(label);
                variationContainer.appendChild(li);

                checkbox.addEventListener("change", updateTable);
            });

            function updateTable() {
                let table = document.getElementById("variation-table");
                let tableHead = table.querySelector("thead tr");
                let tableBody = table.querySelector("tbody");

                let selectedVariations = Array.from(
                    document.querySelectorAll('input[name="variation"]:checked')
                ).map(cb => data.find(v => v.id == cb.value));

                let existingHeaders = tableHead.querySelectorAll("th");
                let firstColumnHeader = existingHeaders[0].textContent; 
                tableHead.innerHTML = `<th>${firstColumnHeader}</th>`;

                selectedVariations.forEach(variation => {
                    let th = document.createElement("th");
                    th.textContent = variation.name;
                    tableHead.appendChild(th);
                });

                let rows = tableBody.querySelectorAll("tr");

                rows.forEach(row => {
                    let property = row.cells[0].textContent.toLowerCase(); 

                    while (row.cells.length > 1) {
                        row.removeChild(row.lastChild);
                    }

                    selectedVariations.forEach(variation => {
                        let valueCell = document.createElement("td");

                        let input = document.createElement("input");
                        input.name = `input-${variation.id}-${property}`;

                        if (variation.datatype === "integer") {
                            input.type = "number";
                            input.step = "1";
                        } else if (variation.datatype === "float") {
                            input.type = "number";
                            input.step = "0.01";
                        } else {
                            input.type = "text";
                        }

                        valueCell.appendChild(input);
                        if (variation.unit) {
                            let unitSpan = document.createElement("span");
                            unitSpan.textContent = ` ${variation.unit}`;
                            valueCell.appendChild(unitSpan);
                        }

                        row.appendChild(valueCell);
                    });
                });
            }
        })
        .catch(error => console.error("Error fetching variations:", error));
}