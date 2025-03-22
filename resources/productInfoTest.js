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
        })
        .catch((error) => console.error("Error fetching data:", error));
}

document.addEventListener("DOMContentLoaded", function () {
    slider = document.querySelector('.slider');
    sliderImages = document.querySelectorAll('.slider img');
    totalImages = sliderImages.length;
    imageWidth = 155; // Chiều rộng mỗi ảnh (145px + 10px margin)
    sliderImages[0].classList.add('active');
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

    productData.productItems.forEach(item => {
        item.customizations.forEach(customization => {
            if (!customizationMap[customization.name]) {
                customizationMap[customization.name] = new Set();
            }
            customizationMap[customization.name].add(customization.value + (customization.unit ? ' ' + customization.unit : ''));
        });
    });

    const customizationContainer = document.getElementById("customizations");
    for (const [type, values] of Object.entries(customizationMap)) {
        const container = document.createElement("div");
        container.classList.add("row");
        container.classList.add("customization-group");

        const title = document.createElement("h3");
        title.textContent = type;
        container.appendChild(title);

        const optionsContainer = document.createElement("div");
        optionsContainer.classList.add("options");

        values.forEach(value => {
            const button = document.createElement("button");
            button.textContent = value;
            button.classList.add("customization-option");
            button.addEventListener("click", () => handleSelection(type, value));
            optionsContainer.appendChild(button);
        });

        container.appendChild(optionsContainer);
        customizationContainer.appendChild(container);
    }
});

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
    } else {
        console.log("No matching product available.");
        
        const productItemIdInput = document.getElementById("productItemId");
        productItemIdInput.value = "";
        inputQuantity.value = "Out of stock";
    }

    currentMaxQuantity = matchingItem.stock;

    updateQuantity(0);
}