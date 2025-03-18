function fetchProducts(query, filter, shopId) {
    // let query = document.getElementById("searchBox").value;
    var url = new URL("https://" + location.host + contextPath + "/ajax/products");
    // let filter;
    // try {
    //     filter = document.querySelector('input[name="filter"]:checked').value;
    // } catch (e) {
    //     filter = 'All';
    // }

    if (query) {
        url.searchParams.append('query', encodeURIComponent(query));
        // url += "?query=" + encodeURIComponent(query);
    }

    if (filter) {
        url.searchParams.append('category', filter.value)
    }

    if (shopId) {
        url.searchParams.append('shopId', shopId);
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let tableBody = document.getElementById("productTable");
            tableBody.innerHTML = "<tr><th>Shop ID</th><th>Category ID</th><th>Name</th><th>Description</th><th>Thumbnail Path</th></tr>";

            data.forEach(item => {
                let row = document.createElement("tr");
                let cell;

                cell = document.createElement("td");
                let link = document.createElement("a");
                link.href = contextPath + "/shop?shopId=" + item.shop.id;
                link.textContent = item.shop.name;
                cell.appendChild(link);
                // cell.textContent = item.shopId;
                row.appendChild(cell);

                cell = document.createElement("td");
                link = document.createElement("a");
                link.href = contextPath + "/category?categoryId=" + item.category.id;
                link.textContent = item.category.name;
                cell.appendChild(link);
                // cell.textContent = item.categoryId;
                row.appendChild(cell);

                cell = document.createElement("td");
                link = document.createElement("a");
                link.href = contextPath + "/product?productId=" + item.id; // Set the URL
                link.textContent = item.name; // Set the link text
                cell.appendChild(link);
                // cell.textContent = item.name;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.textContent = item.description;
                row.appendChild(cell);

                cell = document.createElement("td");
                let image = document.createElement("img");
                image.src = contextPath + "/resources/" + item.thumbnail;
                cell.appendChild(image);
                // cell.textContent = item.thumbnailId;
                row.appendChild(cell);

                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}

function fetchByQueryAndCategory() {
    fetchProducts(document.getElementById("searchBox").value, document.querySelector('input[name="filter"]:checked'), null);
}

function fetchByCategory() { // store category in global
    fetchProducts(null, category, null);
}

function fetchByShop() { // store shop in global
    fetchProducts(null, document.querySelector('input[name="filter"]:checked'), shopId);
}