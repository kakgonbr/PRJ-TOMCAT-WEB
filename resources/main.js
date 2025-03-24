function setTheme(mode = 'auto') {
    const userMode = localStorage.getItem('bs-theme');
    const sysMode = window.matchMedia(
        '(prefers-color-scheme: light)'
    ).matches;
    const useSystem = mode === 'system' || (!userMode && mode === 'auto');
    const modeChosen = useSystem
        ? 'system'
        : mode === 'dark' || mode === 'light'
            ? mode
            : userMode;

    if (useSystem) {
        localStorage.removeItem('bs-theme');
    } else {
        localStorage.setItem('bs-theme', modeChosen);
    }

    document.documentElement.setAttribute(
        'data-bs-theme',
        useSystem ? (sysMode ? 'light' : 'dark') : modeChosen
    );
    document
        .querySelectorAll('.button-switch-theme')
        .forEach((e) => e.classList.remove('active'));
    document.getElementById("button-theme-" + modeChosen).classList.add('active');
}

function startupTheme() {
    setTheme();
    document
        .querySelectorAll('.button-switch-theme')
        .forEach((e) => e.addEventListener('click', () => setTheme(e.getAttribute("data-bs-theme-value"))));
    window
        .matchMedia('(prefers-color-scheme: light)')
        .addEventListener('change', () => setTheme());
}

function closeError() {
    document.getElementById("error-container").remove();
}

// can be dangerous
document.addEventListener("DOMContentLoaded", startupTheme);

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
                    <a href="https://kakgonbri.zapto.org:8443/prj/search?categoryId=${child.id}" class="text-decoration-none text-dark blackLineUnderneath">
                        <h5 class="mt-3">${child.name}</h5>
                    </a>
                    <ul class="list-unstyled">`;
        
        child.children.forEach(grandchild => {
            html += `<li><a href="https://kakgonbri.zapto.org:8443/prj/search?categoryId=${grandchild.id}" class="text-decoration-none text-dark blackLineUnderneath">${grandchild.name}</a></li>`;
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
                        <a class="nav-link ${activeClass} text-dark" href="https://kakgonbri.zapto.org:8443/prj/search?categoryId=${category.id}" data-tab="${tabId}">${category.name}</a>
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