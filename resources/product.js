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
        url.searchParams.append('query', encodeURIComponent(query.value));
        // url += "?query=" + encodeURIComponent(query);
    }

    if (filter) {
        url.searchParams.append('category', filter)
    }

    if (shopId) {
        url.searchParams.append('shopId', shopId);
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let tableBody = document.getElementById("productTable");
            tableBody.innerHTML = "<tr><th>Shop</th><th>Category</th><th>Name</th><th>Description</th><th>Thumbnail Path</th></tr>";

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
    filter = document.querySelector('input[name="filter"]:checked');
    fetchProducts(document.getElementById("searchBox"), filter? filter.value : 0, null);
}

function fetchByCategory() { // store category in global
    fetchProducts(null, categoryId, null);
}

function fetchByShop() { // store shop in global
    filter = document.querySelector('input[name="filter"]:checked');
    fetchProducts(null, filter? filter.value : 0, shopId);
}

function getProductInfo(productId) {
    var url = new URL("https://" + location.host + contextPath + "/ajax/products");

    if (productId) {
        url.searchParams.append('productId', productId)
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            console.log(data); // test
        })
        .catch(error => console.error("Error fetching data:", error));
}

function fetchUserShopProducts() {
    var url = new URL("https://" + location.host + contextPath + "/ajax/products");
    url.searchParams.append('action', 'fetchUserShopProducts');

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let tableBody = document.getElementById("productTableShop");
            tableBody.innerHTML = '<thead class="thead-dark"><tr><th>Tên</th><th>Mô tả</th><th>Giá</th></tr></thead>';

            data.forEach(item => {
                let row = document.createElement("tr");

                let cell = document.createElement("td");
                cell.textContent = item.name;
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

window.onload = fetchUserShopProducts;