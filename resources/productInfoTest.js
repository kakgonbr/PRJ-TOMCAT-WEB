let currentIndex = 0;
const imagesPerView = 4;
let slider;
let sliderImages;
let totalImages;
let imageWidth;

function changeImage(img) {
    document.getElementById('mainImage').src = img.src;
    sliderImages.forEach(sliderImg => sliderImg.classList.remove('active'));
    img.classList.add('active');
}

function moveSlider(direction) {
    currentIndex += direction;
    if (currentIndex < 0) {
        currentIndex = 0;
    }
    if (currentIndex > totalImages - imagesPerView) {
        currentIndex = totalImages - imagesPerView;
    }
    const translateX = -currentIndex * imageWidth;
    slider.style.transform = `translateX(${translateX}px)`;
}
// Đặt ảnh đầu tiên là active khi tải trang

let inputQuantity;
let lastValidValue;
let currentMaxQuantity;

function updateQuantity(change) {
    let quantity = parseInt(inputQuantity.value);
    quantity += change;
    if (quantity > currentMaxQuantity) {
        quantity = currentMaxQuantity;
    }
    else if (quantity < 1) {
        quantity = 1;
    }
    inputQuantity.value = quantity;
    lastValidValue = quantity;
}

var productData;
var customizationMap = {};

function getProductInfo(productId) {
    var url = new URL(
        "https://" + location.host + contextPath + "/ajax/products"
    );

    if (productId) {
        url.searchParams.append("productId", productId);
    }

    fetch(url.toString())
        .then((response) => response.json())
        .then((data) => {
            productData = data;

            document.getElementById("product-name").innerText = productData.name;
            document.getElementById("product-desc").innerText = productData.description;

            productData.productItems.forEach(item => {
                item.customizations.forEach(customization => {
                    if (!customizationMap[customization.name]) {
                        customizationMap[customization.name] = new Set();
                    }
                    customizationMap[customization.name].add(customization.value + (customization.unit ? ' ' + customization.unit : ''));
                });
            });

            postFetch();
        })
        .catch((error) => console.error("Error fetching data:", error));
}

document.addEventListener("DOMContentLoaded", function () {
    inputQuantity = document.getElementById('quantity');

    inputQuantity.addEventListener('input', function () {
        const currentValue = this.value;
        if (currentValue === '' || isNaN(currentValue) || parseInt(currentValue) < 1) {
            this.value = lastValidValue;
        } else {
            lastValidValue = parseInt(currentValue);
        }
    });

    lastValidValue = parseInt(inputQuantity.value);

    getProductInfo(productId);
});

function postFetch() {
    for (const [type, values] of Object.entries(customizationMap)) {
        const container = document.getElementById("customizations");
        
        const title = document.createElement("h4");
        title.classList.add("fw-semibold");
        title.classList.add("mb-3");
        title.textContent = type;
        container.appendChild(title);
        
        const optionsContainer = document.createElement("div");
        optionsContainer.classList.add("variation");
        
        values.forEach(value => {
            const optionDiv = document.createElement("div");
            optionDiv.classList.add("option", "mb-2");
            
            const radio = document.createElement("input");
            radio.type = "radio";
            radio.name = type;
            radio.value = value;
            radio.classList.add("variation-value");
            radio.addEventListener("change", () => handleSelection(type, value));
            
            const variationDiv = document.createElement("div");
            variationDiv.classList.add("variation-value-name");
            
            const span = document.createElement("span");
            span.classList.add("span");
            span.textContent = value;
            
            variationDiv.appendChild(span);
            optionDiv.appendChild(radio);
            optionDiv.appendChild(variationDiv);
            optionsContainer.appendChild(optionDiv);
        });
        
        container.appendChild(optionsContainer);
    }

    preselect();

    getImages();

    slider = document.querySelector('.slider');
    sliderImages = document.querySelectorAll('.slider img');
    totalImages = sliderImages.length;
    imageWidth = 155; // Chiều rộng mỗi ảnh (145px + 10px margin)

    if (sliderImages.length > 0) {
        sliderImages[0].classList.add('active');
    }
}

var selectedOptions = {};

function handleSelection(type, value) {
    selectedOptions[type] = value;
    updateSelection();
}

function updateSelection() {
    const matchingItem = productData.productItems.find(item => {
        return item.customizations.every(cust =>
            selectedOptions[cust.name] === (cust.value + (cust.unit ? ' ' + cust.unit : ''))
        );
    });

    if (matchingItem && matchingItem.stock > 0) {
        console.log("Selected Product Item:", matchingItem);
        
        const productItemIdInput = document.getElementById("productItemId");
        productItemIdInput.value = matchingItem.id;

        inputQuantity.value = matchingItem.price;
        currentMaxQuantity = matchingItem.stock;

        updateQuantity(0);
    } else {
        console.log("No matching product available.");
        
        const productItemIdInput = document.getElementById("productItemId");
        productItemIdInput.value = 0;

        currentMaxQuantity = 0;

        updateQuantity(0);
    }

    document.getElementById("stock-counter").innerText = currentMaxQuantity;
}

function preselect() {
    const firstValidItem = productData.productItems.find(item => item.stock > 0);
    if (firstValidItem) {
        firstValidItem.customizations.forEach(cust => {
            selectedOptions[cust.name] = cust.value + (cust.unit ? ' ' + cust.unit : '');
            
            const radioButton = document.querySelector(`input[name="${cust.name}"][value="${selectedOptions[cust.name]}"]`);
            if (radioButton) {
                radioButton.checked = true;
            }
        });
        updateSelection();
    }
}

function getImages() {
    if (!productData) return;
    const current = document.getElementById("current-image");
    current.innerHTML = "";

    const img = document.createElement("img");
    img.src = contextPath + "/resources/" + productData.thumbnail;
    img.classList.add("rounded");
    img.style = "width: 100%; object-fit: cover;";

    current.appendChild(img);

    const container = document.getElementById("product-images-container");
    container.innerHTML = "";


    productData.productImages.forEach((imageSrc) => {
        const img = document.createElement("img");
        img.src = contextPath + "/resources/" + imageSrc;
        img.classList.add("img-slider", "rounded");
        img.setAttribute("onmouseover", "changeImage(this)");

        container.appendChild(img);
    });
}