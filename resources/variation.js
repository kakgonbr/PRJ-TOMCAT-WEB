document.addEventListener("DOMContentLoaded", function () {
    initVariationTable();
});

function initVariationTable() {
    let headerRow = document.getElementById("variationHeader");
    headerRow.innerHTML = `
        <th>Variation</th>
        <th>Options</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Actions</th>
    `;
}

function addVariation() {
    let name = document.getElementById("variationName").value.trim();
    let options = document.getElementById("variationOptions").value.trim();

    if (name === "" || options === "") {
        alert("Please fill in variation name and options.");
        return;
    }

    let optionList = options.split(",").map(opt => opt.trim()).filter(opt => opt !== "");

    if (optionList.length === 0) {
        alert("Please enter at least one valid option.");
        return;
    }

    let tableBody = document.getElementById("variationBody");
    let newRow = document.createElement("tr");

    let variationCell = document.createElement("td");
    variationCell.innerText = name;
    newRow.appendChild(variationCell);

    let optionsCell = document.createElement("td");
    optionsCell.innerText = optionList.join(", ");
    newRow.appendChild(optionsCell);

    let priceCell = document.createElement("td");
    let priceInput = document.createElement("input");
    priceInput.type = "text";
    priceInput.name = `price_${name}`;
    priceCell.appendChild(priceInput);
    newRow.appendChild(priceCell);

    let stockCell = document.createElement("td");
    let stockInput = document.createElement("input");
    stockInput.type = "text";
    stockInput.name = `stock_${name}`;
    stockInput.value = "0";
    stockCell.appendChild(stockInput);
    newRow.appendChild(stockCell);

    let actionCell = document.createElement("td");
    let deleteButton = document.createElement("button");
    deleteButton.innerText = "Remove";
    deleteButton.onclick = function () {
        tableBody.removeChild(newRow);
    };
    actionCell.appendChild(deleteButton);
    newRow.appendChild(actionCell);

    tableBody.appendChild(newRow);

    document.getElementById("variationName").value = "";
    document.getElementById("variationOptions").value = "";
}
