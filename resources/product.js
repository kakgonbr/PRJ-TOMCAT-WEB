function fetchProducts() {
    fetch(contextPath + "/ajax/products")
        .then(response => response.json())
        .then(data => {
            let listElement = document.getElementById("list");
            listElement.innerHTML = "";

            data.forEach(item => {
                let li = document.createElement("li");
                li.textContent = item.id + "\n" + item.shopId + "\n" + item.categoryId + "\n" + item.name + "\n" + item.description + "\n" + item.thumbnailId;
                listElement.appendChild(li);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}