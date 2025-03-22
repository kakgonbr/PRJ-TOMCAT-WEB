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

inputQuantity.addEventListener('input', function() {
    const currentValue = this.value;
    if (currentValue === '' || isNaN(currentValue) || parseInt(currentValue) < 1) {
        this.value = lastValidValue; 
    } else {
        lastValidValue = parseInt(currentValue); 
    }
    }
);

function updateQuantity(change) {
    let quantity = parseInt(inputQuantity.value); 
    quantity += change; 
    if (quantity < 1) {
        quantity = 1; 
    }
    inputQuantity.value = quantity; 
    lastValidValue = quantity; 
}

document.addEventListener("DOMContentLoaded", function () {
    slider = document.querySelector('.slider');
    sliderImages = document.querySelectorAll('.slider img');
    totalImages = sliderImages.length;
    imageWidth = 155; // Chiều rộng mỗi ảnh (145px + 10px margin)
    sliderImages[0].classList.add('active');
    inputQuantity = document.getElementById('quantity');
    lastValidValue = parseInt(inputQuantity.value); 
});