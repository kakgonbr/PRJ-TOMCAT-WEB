function fetchProducts() {
    let query = document.getElementById("searchBox").value;
    let url = contextPath + "/ajax/products"
    if (query) {
        url += "?query=" + encodeURIComponent(query);
    }

    fetch(url)
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

                ent.createElement("td");
                cell.textContent = item.categoryId;
                row.appendChild(cell);

                ent.createElement("td");
                cell.textContent = item.name;
                row.appendChild(cell);

                ent.createElement("td");
                cell.textContent = item.description;
                row.appendChild(cell);

                ent.createElement("td");
                cell.textContent = item.thumbNailId;
                row.appendChild(cell);

                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
        // let li = document.createElement("li");
        // li.textContent = item.id + "\n" + item.shopId + "\n" + item.categoryId + "\n" + item.name + "\n" + item.description + "\n" + item.thumbnailId;
        // listElement.appendChild(li);

}
