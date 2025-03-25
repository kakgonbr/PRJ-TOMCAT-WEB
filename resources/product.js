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
    url.searchParams.append("categoryId", filter);
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
  if (!shopId) {
    console.error("shopId is not defined");
    return;
  }

  var url = new URL("https://" + location.host + contextPath + "/ajax/products");
  url.searchParams.append("shopId", shopId);
  url.searchParams.append("status", status ? "true" : "false"); // Chuyển status thành 'true' hoặc 'false'

  fetch(url.toString())
    .then((response) => response.json())
    .then((data) => {
      let tableContainer = document.getElementById("productTableShop");
      tableContainer.innerHTML = ""; // Xóa dữ liệu cũ

      // Bọc bảng trong div để hỗ trợ hiển thị trên mobile
      let responsiveDiv = document.createElement("div");
      responsiveDiv.className = "table-responsive";

      // Tạo bảng Bootstrap
      let table = document.createElement("table");
      table.className = "table table-striped table-hover table-bordered align-middle";

      // Tạo tiêu đề bảng
      let thead = document.createElement("thead");
      thead.className = "table-dark";
      thead.innerHTML = `
        <tr>
          <th scope="col">Shop</th>
          <th scope="col">Category</th>
          <th scope="col">Name</th>
          <th scope="col">Description</th>
          <th scope="col" class="text-center">Actions</th>
        </tr>
      `;
      table.appendChild(thead);

      // Tạo phần thân bảng
      let tbody = document.createElement("tbody");

      data.forEach((item) => {
        let row = document.createElement("tr");

        row.innerHTML = `
          <td><a href="${contextPath}/shop?shopId=${item.shop.id}" class="text-decoration-none">${item.shop.name}</a></td>
          <td><a href="${contextPath}/category?categoryId=${item.category.id}" class="text-decoration-none">${item.category.name}</a></td>
          <td><a href="${contextPath}/product?productId=${item.id}" class="text-decoration-none fw-bold">${item.name}</a></td>
          <td class="text-wrap" style="max-width: 300px;">${item.description}</td>
          <td class="text-center">
            ${item.status
              ? `
                <button class="btn btn-warning btn-sm me-2" onclick="window.location.href='${contextPath}/product?action=edit&productId=${item.id}'">
                  Edit
                </button>
                <button class="btn btn-danger btn-sm" onclick="if (confirm('Are you sure you want to delete this product?')) {
                  fetch('${contextPath}/product', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: 'action=delete&productId=${encodeURIComponent(item.id)}'
                  }).then(() => fetchProductsShop(${shopId}, true));
                }">
                  Delete
                </button>`
              : `
                <button class="btn btn-success btn-sm" onclick="if (confirm('Do you want to restore this product?')) {
                  fetch('${contextPath}/product', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: 'action=restore&productId=${encodeURIComponent(item.id)}'
                  }).then(() => fetchProductsShop(${shopId}, false));
                }">
                  Restore
                </button>`
            }
          </td>
        `;

        tbody.appendChild(row);
      });

      table.appendChild(tbody);
      responsiveDiv.appendChild(table);
      tableContainer.appendChild(responsiveDiv);
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
              <a href="https://kakgonbri.zapto.org:8443/prj/product?productId=${product.id}" class="text-dark text-decoration-none">
                  <div class="card">
                      <div class="position-relative">
                          <img src="/prj/resources/${product.thumbnail}" alt="${product.name}" loading="lazy" class="w-100">
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
/*fetch products for search */
async function fetchProductsSearch() {
  const container = document.querySelector(".col-9.row");
  container.innerHTML = "";
  
  try {
      var checkedBoxes = document.querySelectorAll('input[name=categoryFilter]:checked');
      var url = new URL(
        "https://" + location.host + contextPath + "/ajax/products"
      );

      url.searchParams.append("query", query);

      checkedBoxes.forEach(item => url.searchParams.append("categoryId", item.value));

      const response = await fetch(url.toString());
      const products = await response.json();
      
      products.forEach(product => {
          const productCard = document.createElement("div");
          productCard.classList.add("col-3");
          productCard.innerHTML = `
              <a href="https://kakgonbri.zapto.org:8443/prj/product?productId=${product.id}" class="text-dark text-decoration-none">
                  <div class="card">
                      <div class="position-relative">
                          <img src="/prj/resources/${product.thumbnail}" alt="${product.name}" loading="lazy" class="w-100">
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
      console.error("Error for fetching products for search:", error);
  }
}

function createProductElement(product) {
  let li = document.createElement("li");

  // Tạo checkbox để chọn sản phẩm
  let checkbox = document.createElement("input");
  checkbox.type = "checkbox";
  checkbox.value = product.id;
  checkbox.classList.add("product-checkbox");

  // Nhãn hiển thị tên sản phẩm
  let label = document.createElement("label");
  label.appendChild(checkbox);
  label.appendChild(document.createTextNode(" " + product.name));

  li.appendChild(label);
  return li;
}


