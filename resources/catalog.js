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
        <input class="form-check-input" type="checkbox" id="${categoryId}" value="${categoryId}" name="categoryFilter">
        <label class="form-check-label" for="${categoryId}" data-bs-toggle="collapse" data-bs-target="#${collapseId}">
            ${category.name}
        </label>`;
    
    if (category.children && category.children.length > 0) {
        html += `<div class="collapse my-1" id="${collapseId}">
                    <div class="collapse-content" id="${collapseId}Content">`;
        
        category.children.forEach(child => {
            html += generateCategoryHTML(child, `${collapseId}Content`);
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
}

document.addEventListener("DOMContentLoaded", () => {
    fetchCategories();
});

/*header */
async function fetchCategoriesHeader() {
    try {
        let response = await fetch('https://kakgonbri.zapto.org:8443/prj/ajax/category?categoryId=0');
        let data = await response.json();
        renderTabs(data.children);
    } catch (error) {
        console.error('Error fetching category data:', error);
    }
}

function generateTabContent(category) {
    let tabId = `content${category.id}`;
    let html = `<div class="category-content" id="${tabId}" style="display: none;">
                    <div class="row">`;
    
    category.children.forEach(child => {
        html += `<div class="col">
                    <a href="https://kakgonbri.zapto.org:8443/prj/testcatalog?categoryId=${child.id}" class="text-decoration-none text-dark blackLineUnderneath">
                        <h5 class="mt-3">${child.name}</h5>
                    </a>
                    <ul class="list-unstyled">`;
        
        child.children.forEach(grandchild => {
            html += `<li><a href="https://kakgonbri.zapto.org:8443/prj/testcatalog?categoryId=${grandchild.id}" class="text-decoration-none text-dark blackLineUnderneath">${grandchild.name}</a></li>`;
        });
        
        html += `</ul>
                </div>`;
    });
    
    html += `</div>
            </div>`;
    return html;
}

function renderTabs(categories) {
    let tabContainer = document.getElementById("categoryTabs");
    let contentContainer = document.querySelector(".tab-content");
    
    if (!tabContainer || !contentContainer) return;

    let tabHtml = "";
    let contentHtml = "";

    categories.forEach((category, index) => {
        let activeClass = index === 0 ? "active" : "";
        let tabId = `content${category.id}`;

        tabHtml += `<li class="nav-item">
                        <a class="nav-link ${activeClass} text-dark" href="https://kakgonbri.zapto.org:8443/prj/testcatalog?categoryId=${category.id}" data-tab="${tabId}">${category.name}</a>
                    </li>`;

        contentHtml += generateTabContent(category);
    });

    tabContainer.innerHTML = tabHtml;
    contentContainer.innerHTML = contentHtml;

    let firstContent = document.querySelector(".category-content");
    if (firstContent) firstContent.style.display = "block";

    document.querySelectorAll("#categoryTabs .nav-link").forEach(tab => {
        tab.addEventListener("mouseover", function (e) {
            e.preventDefault();

            document.querySelectorAll(".category-content").forEach(content => {
                content.style.display = "none";
            });

            let selectedContent = document.getElementById(this.getAttribute("data-tab"));
            if (selectedContent) selectedContent.style.display = "block";

            document.querySelectorAll("#categoryTabs .nav-link").forEach(nav => nav.classList.remove("active"));
            this.classList.add("active");
        });
    });
}


document.addEventListener("DOMContentLoaded", fetchCategoriesHeader);



