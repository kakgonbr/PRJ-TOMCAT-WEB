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
        .then(response => response.json())
        .then(data => {
            data.forEach(shop => {
                let container = document.getElementById("shopContainer");
                let ul = document.createElement("ul");
                ul.innerText = "Shop:"
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
        .catch(error => console.error("Error fetching data:", error));
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

            accordions.forEach(item => {
                let button = item.querySelector(".accordion-button");
                let collapseId = button.getAttribute("data-bs-target");
                let collapse = document.querySelector(collapseId);
                let bsCollapse = new bootstrap.Collapse(collapse, { toggle: false });

                if (button.textContent.trim().toLowerCase().includes(searchValue)) {
                    bsCollapse.show();
                    found = true;
                } else {
                    let links = collapse.querySelectorAll("a");
                    links.forEach(link => {
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
