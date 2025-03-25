function fetchCategory() {
  fetch(contextPath + "/ajax/category")
  .then(response => response.json())
  .then(data => {
    let treeContainer = document.getElementById("categoryFilter");
    treeContainer.innerHTML = "";
    treeContainer.appendChild(createCategoryElement(data));
  })
  .catch(error => console.error("Error fetching filters:", error));
  if (category && category !== "") {
    
  }
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




