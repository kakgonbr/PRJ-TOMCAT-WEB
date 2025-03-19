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