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
        let response = await fetch('https://kakgonbri.zapto.org:8443/prj/ajax/category?categoryId=1');
        let data = await response.json();
        renderCategories(data.children);
    } catch (error) {
        console.error('Error fetching category data:', error);
    }
}

function generateCategoryHTML(category, parentId = "categoriesCollapseContent") {
    let categoryId = `category${category.id}`;
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