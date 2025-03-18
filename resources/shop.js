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