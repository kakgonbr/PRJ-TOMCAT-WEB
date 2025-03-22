//clear btn 
const searchBar = document.querySelector('#searchBar');
const clearButton = document.querySelector('.clear-btn');

clearButton.addEventListener('click', () => {
    searchBar.value = '';
    clearButton.style.display = 'none';
});   
searchBar.addEventListener('input',() => {
    clearButton.style.display = searchBar.value ? 'block':'none';
});
//dropdown menu display
const category = document.getElementById('category');
const menu = document.querySelector('.dropdown-menu-hover');
let timeoutId = null;

function showMenu() {
    clearTimeout(timeoutId); 
    menu.style.display = 'block';
}

function hideMenuAfterDelay() {
    timeoutId = setTimeout(() => {
        menu.style.display = 'none';
    }, 300); 
}

category.addEventListener('mouseenter', showMenu);
category.addEventListener('mouseleave', hideMenuAfterDelay);
menu.addEventListener('mouseenter', showMenu);
menu.addEventListener('mouseleave', hideMenuAfterDelay);
/*category */
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