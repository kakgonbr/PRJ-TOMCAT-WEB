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
    if (quantity >= currentMaxQuantity) {
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
            
            let anchor = document.createElement("a");
            anchor.href = contextPath + "/shop?shopId=" + productData.shop.id;
            anchor.innerText = productData.shop.name;
            let header = document.getElementById("product-shop");
            header.innerText = "Shop: ";
            header.appendChild(anchor);
            
            anchor = document.createElement("a");
            anchor.href = contextPath + "/category?categoryId=" + productData.category.id;
            anchor.innerText = productData.category.name;
            header = document.getElementById("product-category");
            header.innerText = "Category: ";
            header.appendChild(anchor);

            if (productData.promotion) {
                let container = document.getElementById("promotion-container");
                header = document.createElement("h3");
                header.innerText = productData.promotion.name + " : - " + productData.promotion.value + (productData.promotion.type ? "$" : "%");
                container.appendChild(header);
    
                header = document.createElement("h3");
                header.innerText = "Expire on: " + productData.promotion.expireDate;
                container.appendChild(header);
            }

            productData.productItems.forEach(item => {
                item.customizations.forEach(customization => {
                    if (!customizationMap[customization.name]) {
                        customizationMap[customization.name] = new Set();
                    }
                    customizationMap[customization.name].add(customization.value + (customization.unit ? ' ' + customization.unit : ''));
                });
            });

            postFetch();
            //load reviews
            loadReviews(productId);
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
        // console.log("Selected Product Item:", matchingItem);
        
        const productItemIdInput = document.getElementById("productItemId");
        const price = document.getElementById("price-counter");
        
        productItemIdInput.value = matchingItem.id;

        // productData.promotion.value + (productData.promotion.type ? "$" : "%")
        if (matchingItem.promotion) {
            price.innerHTML = "<del>" + matchingItem.price + "$</del>" + " " + (matchingItem.promotion.type ? matchingItem.price - parseInt(matchingItem.promotion.value) : matchingItem.price * (100.0 - parseInt(matchingItem.promotion.value)) / 100.0) + "$";
        } else {
            price.innerHTML = matchingItem.price + "$";
        }

        currentMaxQuantity = matchingItem.stock;
        
        updateQuantity(0);
    } else {
        // console.log("No matching product available.");
        
        const price = document.getElementById("price-counter");
        const productItemIdInput = document.getElementById("productItemId");

        productItemIdInput.value = 0;
        price.innerHTML = "Out of Stock";
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

    const img = document.getElementById("mainImage");
    img.src = contextPath + "/resources/" + productData.thumbnail;

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

/*review */
function loadReviews(productId) {
    fetch("https://kakgonbri.zapto.org:8443/prj/ajax/reviewloader?productId=" + productId)
        .then(response => response.json())
        .then(reviews => {
            // Xử lý average rating
            if (reviews.length === 0) {
                document.getElementById('average-rating').textContent = "0.0";
            } else {
                const totalRating = reviews.reduce((sum, review) => sum + review.rate, 0);
                const averageRating = (totalRating / reviews.length).toFixed(1);
                document.getElementById('average-rating').textContent = averageRating;
            }

            // Xử lý hiển thị reviews
            const container = document.getElementById('reviews-container');
            container.innerHTML = ''; 

            if (reviews.length === 0) {
                container.innerHTML = '<p>No reviews yet.</p>';
                return;
            }

            reviews.forEach(review => {
                const reviewElement = createReviewElement(review);
                container.appendChild(reviewElement);
            });
        })
        .catch(error => {
            console.error('Error loading reviews:', error);
            document.getElementById('average-rating').textContent = "N/A";
            document.getElementById('reviews-container').innerHTML = 
                '<p class="text-danger">Error loading reviews. Please try again later.</p>';
        });
}

function createReviewElement(review) {
    const div = document.createElement('div');
    div.className = 'review-card';
    
    const stars = '★'.repeat(review.rate) + '☆'.repeat(5 - review.rate);
    
    div.innerHTML = `
        <div class="review-user">
            <img src="${contextPath}/resources/${review.profileStringResourceId}" 
                 alt="User Avatar" 
                 class="review-user-avatar">
            <span class="review-user-name">${review.userName}</span>
        </div>
        <div class="review-content">
            <div class="review-header">
                <div class="rating">
                    ${stars}
                </div>
                <div class="review-id">
                    Review #${review.id}
                </div>
            </div>
            <div class="review-comment">
                ${review.comment || 'No comment'}
            </div>
        </div>
    `;
    
    return div;
}