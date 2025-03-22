function fetchProducts(query, filter, shopId) {
  // let query = document.getElementById("searchBox").value;
  var url = new URL(
    "https://" + location.host + contextPath + "/ajax/products"
  );
  // let filter;
  // try {
  //     filter = document.querySelector('input[name="filter"]:checked').value;
  // } catch (e) {
  //     filter = 'All';
  // }

  if (query) {
    url.searchParams.append("query", encodeURIComponent(query.value));
    // url += "?query=" + encodeURIComponent(query);
  }

  if (filter) {
    url.searchParams.append("category", filter);
  }

  if (shopId) {
    url.searchParams.append("shopId", shopId);
  }

  fetch(url.toString())
    .then((response) => response.json())
    .then((data) => {
      let tableBody = document.getElementById("productTable");
      tableBody.innerHTML =
        "<tr><th>Shop</th><th>Category</th><th>Name</th><th>Description</th><th>Thumbnail Path</th></tr>";

      data.forEach((item) => {
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
    .catch((error) => console.error("Error fetching data:", error));
}

function fetchByQueryAndCategory() {
  filter = document.querySelector('input[name="filter"]:checked');
  fetchProducts(
    document.getElementById("searchBox"),
    filter ? filter.value : 0,
    null
  );
}

function fetchByCategory() {
  // store category in global
  fetchProducts(null, categoryId, null);
}

function fetchByShop() {
  // store shop in global
  filter = document.querySelector('input[name="filter"]:checked');
  fetchProducts(null, filter ? filter.value : 0, shopId);
}

function fetchProductsShop(shopId, status) {
  var url = new URL(
    "https://" + location.host + contextPath + "/ajax/products"
  );

  if (shopId) {
    url.searchParams.append("shopId", shopId);
  } if (status) {
    url.searchParams.append("status", status ? "true" : "false"); //fetch into T or F

  }

  fetch(url.toString())
    .then((response) => response.json())
    .then((data) => {
      let tableBody = document.getElementById("productTableShop");
      tableBody.innerHTML =
        "<tr><th>Shop</th><th>Category</th><th>Name</th><th>Description</th><th>Actions</th></tr>";

      data.forEach((item) => {
        let row = document.createElement("tr");
        let cell;

        // Shop
        cell = document.createElement("td");
        let link = document.createElement("a");
        link.href = contextPath + "/shop?shopId=" + item.shop.id;
        link.textContent = item.shop.name;
        cell.appendChild(link);
        row.appendChild(cell);

        // Category
        cell = document.createElement("td");
        link = document.createElement("a");
        link.href = contextPath + "/category?categoryId=" + item.category.id;
        link.textContent = item.category.name;
        cell.appendChild(link);
        row.appendChild(cell);

        // Name
        cell = document.createElement("td");
        link = document.createElement("a");
        link.href = contextPath + "/product?productId=" + item.id;
        link.textContent = item.name;
        cell.appendChild(link);
        row.appendChild(cell);

        // Description
        cell = document.createElement("td");
        cell.textContent = item.description;
        row.appendChild(cell);

        // Actions
        cell = document.createElement("td");

        if (item.status === true) {
          let editButton = document.createElement("button");
          editButton.textContent = "Edit";
          editButton.onclick = function () {
            window.location.href =
              contextPath + "/product?action=edit&productId=" + item.id;
          };
          cell.appendChild(editButton);

          let deleteButton = document.createElement("button");
          deleteButton.textContent = "Delete";
          deleteButton.onclick = function () {
            if (confirm("Are you sure you want to delete this product?")) {
              fetch(contextPath + "/product", {
                method: "POST",
                headers: {
                  "Content-Type": "application/x-www-form-urlencoded",
                },
                body: "action=delete&productId=" + encodeURIComponent(item.id),
              }).then(() => fetchProductsShop(shopId, true)); // Refresh list
            }
          };
          cell.appendChild(deleteButton);
        } else {
          let restoreButton = document.createElement("button");
          restoreButton.textContent = "Restore";
          restoreButton.onclick = function () {
            if (confirm("Do you want to restore this product?")) {
              fetch(contextPath + "/product", {
                method: "POST",
                headers: {
                  "Content-Type": "application/x-www-form-urlencoded",
                },
                body: "action=restore&productId=" + encodeURIComponent(item.id),
              }).then(() => fetchProductsShop(shopId, false)); // Refresh list
            }
          };
          cell.appendChild(restoreButton);
        }

        row.appendChild(cell);
        tableBody.appendChild(row);
      });
    })
    .catch((error) => console.error("Error fetching data:", error));
}

/*fetch products for home page */
async function fetchProductsHomePage() {
  const container = document.querySelector(".row.gy-4");
  
  try {
      const response = await fetch("https://kakgonbri.zapto.org:8443/prj/ajax/products?");
      const products = await response.json();
      
      products.forEach(product => {
          const productCard = document.createElement("div");
          productCard.classList.add("col-3", "mb-2");
          productCard.innerHTML = `
              <a href="#" class="text-dark text-decoration-none">
                  <div class="card">
                      <div class="position-relative">
                          <img src="${product.thumbnail}" alt="${product.name}" loading="lazy" class="w-100">
                      </div>
                      <div class="card-body d-flex flex-column justify-content-between">
                          <p class="card-title mt-3 fw-semibold blackLineUnderneath">${product.name}</p>
                          <input type="hidden" value="${product.id}">
                      </div>
                  </div>
              </a>
          `;
          container.appendChild(productCard);
      });
  } catch (error) {
      console.error("Error for fetching products for homepage:", error);
  }
}