function fetchProducts() {
    let query = document.getElementById("searchBox").value;
    var url = new URL("https://" + location.host + "/" + contextPath + "/ajax/products");
    let filter;
    try {
        filter = document.querySelector('input[name="filter"]:checked').value;
    } catch (e) {
        filter = 'All';
    }
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