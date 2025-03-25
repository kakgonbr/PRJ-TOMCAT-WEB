/*price range input*/
let rangeInput = document.querySelectorAll('.range-input input');
let rangeText = document.querySelectorAll('.range-text input');
let progress = document.querySelector('.progress');
let priceMax= rangeInput[0].max;
const priceGap = 1000;
        
rangeInput.forEach(input => {
    input.addEventListener('input', (e) => {
        let minVal = parseInt(rangeInput[0].value);
        let maxVal = parseInt(rangeInput[1].value);
        if(maxVal - minVal < priceGap) {
            if(e.target.className === 'range-min'){
                rangeInput[0].value = maxVal - priceGap;
            }else{
                rangeInput[1].value = minVal + priceGap;
            }
        }
        else {
            let positionMin = (minVal / priceMax) * 100;
            let positionMax  = 100 - ((maxVal / priceMax) * 100);
            progress.style.left = positionMin + '%';
            progress.style.right = positionMax + '%';
            rangeText[0].value = minVal;
            rangeText[1].value = maxVal;
        }
    })
});

rangeText.forEach( (input) => {
    input.addEventListener('input', e =>{
        let minVal = parseInt(rangeText[0].value);
        let maxVal = parseInt(rangeText[1].value);
        
        if((maxVal - minVal >= priceGap) && maxVal <= rangeInput[1].max){
            if(e.target.className == 'form-control input-min'){
                rangeInput[0].value = minVal;
                progress.style.left = ((minVal / priceMax) * 100) + "%";
            }else{
                rangeInput[1].value = maxVal;
                progress.style.right = 100 - (maxVal / priceMax) * 100 + "%";
            }
        }
    });
});

/*Sort by*/
const dropdownItems = document.querySelectorAll(".dropdown-item");
const chosenElement = document.querySelector(".sort-menu-chosen");

dropdownItems.forEach(item => {
        item.addEventListener("click", function (event) {
        chosenElement.textContent = this.textContent.trim();
    });
});

/**/
async function fetchCategories() {
    try {
        let response = await fetch('https://kakgonbri.zapto.org:8443/prj/ajax/category?categoryId='+ categoryId);
        let data = await response.json();
        renderCategories(data.children);
    } catch (error) {
        console.error('Error fetching category data:', error);
    }
}

function generateCategoryHTML(category, parentId = "categoriesCollapseContent") {
    let categoryId = `${category.id}`;
    let collapseId = `collapse${category.id}`;
    
    let html = `<div class="form-check">
        <input class="form-check-input" type="checkbox" id="${categoryId}" value="${categoryId}" name="categoryFilter" data-parent="${parentId}">
        <label class="form-check-label" for="${categoryId}" data-bs-toggle="collapse" data-bs-target="#${collapseId}">
            ${category.name}
        </label>`;

    if (category.children && category.children.length > 0) {
        // this is awful
        html = `<div class="form-check">
        <input class="form-check-input" type="checkbox" id="${categoryId}" value="${categoryId}" name="categoryFilter" data-parent="${parentId}">
        <label class="form-check-label" data-bs-toggle="collapse" data-bs-target="#${collapseId}">
            ${category.name} <i class="bi bi-chevron-down"></i>
        </label>`;
    
        html += `<div class="collapse my-1" id="${collapseId}">
                    <div class="collapse-content" id="${collapseId}Content">`;
        
        category.children.forEach(child => {
            html += generateCategoryHTML(child, `${categoryId}`); // id is current category id
        });
        
        html += `</div>
                </div>`;
    }
    
    html += `</div>`;
    return html;
}

function renderCategories(categories) {
    let container = document.getElementById("categoriesCollapseContent");
    if (!container) return;
    
    let html = "";
    categories.forEach(category => {
        html += generateCategoryHTML(category);
    });
    
    container.innerHTML = html;
    attachCheckboxHandlers(); // Attach event listeners after rendering categories
}

function attachCheckboxHandlers() {
    document.querySelectorAll(".form-check-input").forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            let parentId = this.dataset.parent;
            let children = document.querySelectorAll(`[data-parent='${this.id}']`);
            
            if (children.length > 0) {
                children.forEach(child => {
                    child.checked = this.checked;
                });
            }

            if (!this.checked && parentId !== "categoriesCollapseContent") {
                let parentCheckbox = document.getElementById(parentId);
                if (parentCheckbox) parentCheckbox.checked = false;
            }

            if (parentId !== "categoriesCollapseContent") {
                let parentCheckbox = document.getElementById(parentId);
                if (parentCheckbox) {
                    let allSiblings = document.querySelectorAll(`[data-parent='${parentId}']`);
                    let allChecked = [...allSiblings].every(sibling => sibling.checked);
                    parentCheckbox.checked = allChecked;
                }
            }
        });
    });
}

/*fetch products for search */
async function fetchProductsSearch(byParam = false) {
    const container = document.querySelector(".col-9.row");
    container.innerHTML = "";
    
    try {
        var checkedBoxes = document.querySelectorAll('input[name=categoryFilter]:checked');
        var url = new URL(
          "https://" + location.host + contextPath + "/ajax/products"
        );
  
        url.searchParams.append("query", query);
  
        if (byParam && categoryId !== '') {
            url.searchParams.append("categoryId", categoryId);
        } else {
            checkedBoxes.forEach(item => url.searchParams.append("categoryId", item.value));
        }
  
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

document.addEventListener("DOMContentLoaded", () => {
    fetchCategories();
});


