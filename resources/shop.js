function fetchShops() {
  // let query = document.getElementById("searchBox").value;
  var url = new URL("https://" + location.host + contextPath + "/ajax/shops");
  // let filter;
  // try {
  //     filter = document.querySelector('input[name="filter"]:checked').value;
  // } catch (e) {
  //     filter = 'All';
  // }

  fetch(url.toString())
    .then((response) => response.json())
    .then((data) => {
      data.forEach((shop) => {
        let container = document.getElementById("shopContainer");
        let ul = document.createElement("ul");
        ul.innerText = "Shop:";
        let li = document.createElement("li");
        li.innerText = "ID: " + shop.id;
        ul.appendChild(li);
        li = document.createElement("li");
        li.innerText = "Name: ";
        let anchor = document.createElement("a");
        anchor.href = contextPath + "/shop?shopId=" + shop.id;
        anchor.innerText = shop.name;
        li.appendChild(anchor);
        ul.appendChild(li);

        li = document.createElement("li");
        let img = document.createElement("img");
        img.src = contextPath + "/resources/" + shop.profileResource;
        li.appendChild(img);
        ul.appendChild(li);

        container.appendChild(ul);
      });
    })
    .catch((error) => console.error("Error fetching data:", error));
}

function handleAccordionSearch(searchBoxId, accordionContainerId) {
  let searchBox = document.getElementById(searchBoxId);
  let accordionContainer = document.getElementById(accordionContainerId);

  if (!searchBox || !accordionContainer) return;

  searchBox.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      event.preventDefault();

      let searchValue = searchBox.value.trim().toLowerCase();
      let found = false;

      let accordions = accordionContainer.querySelectorAll(".accordion-item");

      accordions.forEach((item) => {
        let button = item.querySelector(".accordion-button");
        let collapseId = button.getAttribute("data-bs-target");
        let collapse = document.querySelector(collapseId);
        let bsCollapse = new bootstrap.Collapse(collapse, { toggle: false });

        if (button.textContent.trim().toLowerCase().includes(searchValue)) {
          bsCollapse.show();
          found = true;
        } else {
          let links = collapse.querySelectorAll("a");
          links.forEach((link) => {
            if (link.textContent.trim().toLowerCase().includes(searchValue)) {
              bsCollapse.show();
              found = true;
            }
          });
        }
      });

      if (!found) {
        alert("Không tìm thấy kết quả!");
      }
    }
  });
}

function fetchOrders(shopId) {
  if (!shopId) {
    console.error("Missing shopId");
    return;
  }

  var url = new URL("https://" + location.host + contextPath + "/ajax/order");
  url.searchParams.append("shopId", shopId);

  fetch(url.toString())
    .then((response) => response.json())
    .then((data) => {
      let tableContainer = document.getElementById("orderTable");
      tableContainer.innerHTML = "";

      let table = document.createElement("table");
      table.className = "table table-striped table-hover table-bordered";

      let thead = document.createElement("thead");
      thead.className = "table-dark";
      thead.innerHTML = `
          <tr>
            <th scope="col">User Name</th>
            <th scope="col">Product Name</th>
            <th scope="col">Quantity</th>
            <th scope="col">Total Price</th>
            <th scope="col">Shipping Cost</th>
          </tr>
        `;
      table.appendChild(thead);

      // Tạo phần thân bảng
      let tbody = document.createElement("tbody");

      data.forEach((item) => {
        let row = document.createElement("tr");

        row.innerHTML = `
            <td>${item.userName}</td>
            <td>${item.productName}</td>
            <td class="text-center">${item.quantity}</td>
            <td class="text-end">${item.totalPrice.toFixed(2)} $</td>
            <td class="text-end">${item.shippingCost.toFixed(2)} $</td>
          `;

        tbody.appendChild(row);
      });

      table.appendChild(tbody);

      let responsiveDiv = document.createElement("div");
      responsiveDiv.className = "table-responsive";
      responsiveDiv.appendChild(table);

      tableContainer.appendChild(responsiveDiv);
    })
    .catch((error) => console.error("Error fetching orders:", error));
}

  function fetchNotifications(userId) {
    if (!userId) {
      console.error("Missing userId");
      return;
    }

    var url = new URL(
      "https://" + location.host + contextPath + "/ajax/notifications"
    );
    url.searchParams.append("userId", userId);

    fetch(url.toString())
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch notifications");
        }
        return response.json();
      })
      .then((data) => {
        let container = document.getElementById("notificationTable");
        container.innerHTML = "";

        data.forEach((notification) => {
          let item = document.createElement("div");
          item.className =
            "notification-item " + (notification.isRead ? "read" : "unread");
          item.id = `notif-${notification.id}`;
          item.innerHTML = `
                      <h4>${notification.title}</h4>
                      <p>${notification.body}</p>
                  `;
          item.onclick = () => markNotificationAsRead(notification.id);
          container.appendChild(item);
        });
      })
      .catch((error) => console.error("Error fetching notifications:", error));
  }

  function markNotificationAsRead(notificationId) {
    let formData = new FormData();
    formData.append("notificationIds", notificationId);

    fetch(contextPath + "/notification", {
      method: "POST",
      body: formData,
    })
      .then((response) => response.json()) // Đọc JSON từ server
      .then((data) => {
        if (data.success) {
          let notifItem = document.getElementById(`notif-${notificationId}`);
          if (notifItem) {
            notifItem.classList.remove("unread");
            notifItem.classList.add("read"); // Chuyển màu xanh nhạt
          }
        }
      })
      .catch((error) =>
        console.error("Error marking notification as read:", error)
      );
  }