document.addEventListener('DOMContentLoaded', function() {
    fetchProductsForSlider();
});

function fetchProductsForSlider() {
    fetch("https://kakgonbri.zapto.org:8443/prj/ajax/products?")
        .then(response => response.json())
        .then(products => {
            const slider = document.getElementById('product-slider');
            slider.innerHTML = ''; // Clear existing content
            
            const repeatedProducts = repeatProducts(products, 12);
            
            repeatedProducts.forEach((product, index) => {
                const item = createSliderItem(product, index + 1);
                slider.appendChild(item);
            });
        })
        .catch(error => console.error('Error fetching products:', error));
}

function repeatProducts(products, targetLength) {
    const repeated = [];
    while (repeated.length < targetLength) {
        repeated.push(...products);
    }
    return repeated.slice(0, targetLength);
}

function createSliderItem(product, position) {
    const item = document.createElement('div');
    item.className = 'item';
    item.style.setProperty('--position', position);

    const link = document.createElement('a');
    link.href = `${contextPath}/product?productId=${product.id}`;
    
    const img = document.createElement('img');
    img.src = `${contextPath}/resources/${product.thumbnail}`;
    img.alt = product.name;
    img.className = 'rounded product-slider-img';
    
    // Thêm overlay chứa tên sản phẩm
    const overlay = document.createElement('div');
    overlay.className = 'product-overlay';
    overlay.innerHTML = `
        <div class="product-name">${product.name}</div>
    `;

    link.appendChild(img);
    link.appendChild(overlay);
    item.appendChild(link);
    
    return item;
}