function fetchProducts() {
    let query = document.getElementById("searchBox").value;
    var url = new URL("https://" + location.host + "/" + contextPath + "/ajax/products");
    let filter = document.querySelector('input[name="filter"]:checked').value;
    if (query) {
        url.searchParams.append('query', encodeURIComponent(query));
        // url += "?query=" + encodeURIComponent(query);
    }

    if (filter) {
        url.searchParams.append('category', filter)
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let tableBody = document.getElementById("productTable");
            tableBody.innerHTML = "<tr><th>Product ID</th><th>Shop ID</th><th>Category ID</th><th>Name</th><th>Description</th><th>Thumbnail Path</th></tr>";

            data.forEach(item => {
                let row = document.createElement("tr");
                let cell = document.createElement("td");
                cell.textContent = item.id;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.textContent = item.shopId;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.textContent = item.categoryId;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.textContent = item.name;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.textContent = item.description;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.textContent = item.thumbnailId;
                row.appendChild(cell);

                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}

function fetchCategory() {
    fetch(contextPath + "/ajax/category")
        .then(response => response.json())
        .then(data => {
            let container = document.getElementById("categoryFilter");
            container.innerHTML = "";

            data.forEach((text, index) => {
                let label = document.createElement("label");
                let radio = document.createElement("input");
                radio.type = "radio";
                radio.name = "filter";
                radio.value = text;
                if (index === 0) radio.checked = true;

                label.appendChild(radio);
                label.appendChild(document.createTextNode(" " + text));
                container.appendChild(label);
                container.appendChild(document.createElement("br"));
            });
        })
        .catch(error => console.error("Error fetching filters:", error));
}