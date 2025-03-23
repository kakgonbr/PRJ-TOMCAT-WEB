function fetchCategory() {
    fetch(contextPath + "/ajax/category")
        .then(response => response.json())
        .then(data => {
            let treeContainer = document.getElementById("categoryFilter");
            treeContainer.innerHTML = "";
            treeContainer.appendChild(createCategoryElement(data));
        })
        .catch(error => console.error("Error fetching filters:", error));
}

function createCategoryElement(category) {
    let li = document.createElement("li");
    let label = document.createElement("label");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "filter";
    radio.value = category.id;

    label.appendChild(radio);
    label.appendChild(document.createTextNode(" " + category.name));
    li.appendChild(label);

    if (category.children && category.children.length > 0) {
        let ul = document.createElement("ul");
        category.children.forEach(child => {
            ul.appendChild(createCategoryElement(child));
        });
        li.appendChild(ul);
    }
    return li;
}

function fetchVariations(categoryId) {
    var url = new URL(
        "https://" + location.host + contextPath + "/ajax/variation"
      );

    if (categoryId) {
        url.searchParams.append("categoryId", categoryId);
    }

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            let variationContainer = document.getElementById("variationFilter");
            variationContainer.innerHTML = "";

            let ul = document.createElement("ul");
            data.forEach(variation => {
                ul.appendChild(createVariationElement(variation));
            });

            variationContainer.appendChild(ul);
        })
        .catch(error => console.error("Error fetching variations:", error));
}

    function createVariationElement(variation) {
        let li = document.createElement("li");
        let label = document.createElement("label");
        let radio = document.createElement("input");
        radio.type = "radio";
        radio.name = "variation";
        radio.value = variation.id;
        radio.dataset.name = variation.name;

        radio.addEventListener("change", function () {
            fetchVariationValues(variation.id);
        });

        label.appendChild(radio);
        label.appendChild(document.createTextNode(" " + variation.name));
        li.appendChild(label);

        return li;
    }

function fetchVariationValues(variationId) {
    var url = new URL(
        "https://" + location.host + contextPath + "/ajax/variation"
      );

    if (variationId) {
        url.searchParams.append("variationId", variationId);
    }

    fetch(url.toString())
    .then(response => response.json())
    .then(data => {
        let variationValueContainer = document.getElementById("variationValueFilter");
        variationValueContainer.innerHTML = "";

        let ul = document.createElement("ul");

        // 🔹 Tìm variation có ID khớp
        let variation = data.find(v => v.id == variationId);
        if (variation && variation.values) {
            variation.values.forEach(value => {
                ul.appendChild(createVariationValueElement(value));
            });
        } else {
            console.error("Không tìm thấy values cho variationId:", variationId);
        }

        variationValueContainer.appendChild(ul);
    })
    .catch(error => console.error("Error", error));
}

function createVariationValueElement(value) {
    let li = document.createElement("li");
    let label = document.createElement("label");
    let radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "variationValue";
    radio.value = value.id;
    radio.dataset.name = value.value;
    radio.dataset.parent = value.variationId;

    label.appendChild(radio);
    label.appendChild(document.createTextNode(" " + value.value));
    li.appendChild(label);

    return li;
}

function applyVariation() {
    let selectedVariation = document.querySelector("input[name='variation']:checked");
    if (selectedVariation) {
        variationId = selectedVariation.value;
        fetchVariationValues(variationId);
    } else {
        console.error("No variation selected");
    }
}
