function fetchCategory() {
    fetch(contextPath + "/ajax/category")
        .then(response => response.json())
        .then(data => {
            let container = document.getElementById("categoryFilter");
            container.innerHTML = "";

            data.forEach((filter, index) => {
                let label = document.createElement("label");
                let radio = document.createElement("input");
                radio.type = "radio";
                radio.name = "filter";
                radio.value = filter.name;
                if (index === 0) radio.checked = true;

                label.appendChild(radio);
                label.appendChild(document.createTextNode(" " + filter.name));
                container.appendChild(label);
                container.appendChild(document.createElement("br"));
            });
        })
        .catch(error => console.error("Error fetching filters:", error));
}