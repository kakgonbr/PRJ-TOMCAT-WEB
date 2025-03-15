function fetchData() {
    fetch(contextPath + "/ajax/admin/table")
        .then(response => response.json())
        .then(data => populateTables(data))
        .catch(error => console.error("Error fetching data:", error));
}

// Expected JSON received:
// [
//     {
//         name : "resources"
//         records : []
//     },
//     {
//         name : "users"
//         records : []
//     }
// ]

// make sure the order of the serialized objects match the tables'
function populateTables(data) {
    data.forEach(table => {
        let tbody = document.querySelector(`#${table.name}`);
        if (!tbody) return;
        
        tbody.innerHTML = "";

        table.records.forEach(item => {
            let row = tbody.insertRow();

            Object.values(item).forEach(value => {
                let cell = row.insertCell();
                cell.textContent = value;
            });

            let params = new URLSearchParams();
            Object.entries(item).forEach(([key, value]) => {
                if (value !== null && value !== undefined && value !== "null") {
                    params.append(key, value);
                }
            });
            
            params.append("action", "edit");
            params.append("table", table.name);

            let editCell = row.insertCell();
            let editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.addEventListener("click", function () {
                window.location.href = contextPath + `/admin/cp?${params.toString()}`;
            });
            editCell.appendChild(editButton);

            let deleteCell = row.insertCell();
            let deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.addEventListener("click", function () {
                if (confirm("Are you sure you want to delete this item?")) {
                    window.location.href = contextPath + `/admin/cp?table=${table.name}&action=delete&id=${item.id}`;
                }
            });
            deleteCell.appendChild(deleteButton);
        });
    });
}
// const tableBody = document.getElementById("resource-data-body");
// tableBody.innerHTML = "";

// data.forEach(item => {
//     let row = tableBody.insertRow();

//     Object.values(item).forEach(value => {
//         let cell = row.insertCell();
//         cell.textContent = value;
//     });

//     // Construct query parameters from item properties
//     let params = new URLSearchParams(item).toString();
//     params.append("table", );
//     params.append("action", "edit");

//     // Add Edit button
//     let editCell = row.insertCell();
//     let editButton = document.createElement("button");
//     editButton.textContent = "Edit";
//     editButton.addEventListener("click", function () {
//         window.location.href = contextPath + `/admin/cp?${params}`;
//     });
//     editCell.appendChild(editButton);


//     let deleteCell = row.insertCell();
//     let deleteButton = document.createElement("button");
//     deleteButton.textContent = "Delete";
//     deleteButton.addEventListener("click", function () {
//         if (confirm("Delete?")) {
//             window.location.href = contextPath + `/admin/cp?table=${}&action=delete&id=${item.id}`;
//         }
//     });
//     deleteCell.appendChild(deleteButton);
// });