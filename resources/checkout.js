let addressInput = null;
let suggestionsContainer = null;
let shippingCosts = new Map(); // Lưu trữ shipping cost cho mỗi item
let uniqueShopAddresses = new Map();

function debounce(func, wait) {
    let timeout;
    return function executedFunc(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => { clearTimeout(timeout); func(...args); }, wait);
    };
}

async function calculateShippingCost(shopAddress, userAddress, cartItemId) {
    try {
        const url = `https://kakgonbri.zapto.org:8443/prj/ajax/map?action=shippingFee&addressOrigin=${encodeURIComponent(shopAddress)}&addressDestination=${encodeURIComponent(userAddress)}`;
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        
        if (data.status === 'OK' && typeof data.fee === 'number') {
            shippingCosts.set(cartItemId, data.fee);
            uniqueShopAddresses.set(shopAddress, data.fee);
            
            const cell = document.querySelector(`td[data-cart-item-id="${cartItemId}"]`);
            if (cell) {
                cell.textContent = `${data.fee}`;
            }
            
            updateTotalShippingCost();
        } else {
            throw new Error('Invalid response data');
        }
    } catch (error) {
        console.error('Error calculating shipping cost:', error);
        console.log('Shop address:', shopAddress);
        console.log('User address:', userAddress);
        alert('Không thể tính phí vận chuyển. Vui lòng thử lại.');
    }
}

function updateTotalShippingCost() {
    const totalShippingCost = Array.from(uniqueShopAddresses.values())
        .reduce((sum, cost) => sum + cost, 0);
    
    const totalShippingElement = document.getElementById('totalShippingCost');
    if (totalShippingElement) {
        totalShippingElement.textContent = `${totalShippingCost}`;
    }
    updateGrandTotal(totalShippingCost);
}

function updateGrandTotal(shippingCost) {
    const grandTotalElement = document.getElementById('grandTotal');
    if (!grandTotalElement) return;

    let subtotal = parseFloat(document.querySelector('.price-summary .text-primary, .price-summary .text-success').textContent);
    
    const newTotal = subtotal + shippingCost;
    
    grandTotalElement.textContent = `${newTotal}`;
}

async function updateAllShippingCosts(userAddress) {
    if (!userAddress) return;
    
    // Reset map 
    shippingCosts.clear();
    uniqueShopAddresses.clear();
    
    const shippingCells = document.querySelectorAll('td.shipping-cost');
    const promises = Array.from(shippingCells).map(cell => {
        let shopAddress = cell.dataset.shopAddress;
        let cartItemId = cell.dataset.cartItemId;
        return calculateShippingCost(shopAddress, userAddress, cartItemId);
    });
    
    await Promise.all(promises);
}

function initializeAddressSearch() {
    addressInput = document.getElementById('address');
    suggestionsContainer = document.getElementById('suggestions');

    if (!addressInput || !suggestionsContainer) {
        console.error('Required elements not found');
        return;
    }

    const search = debounce((query) => {
        if (query.length < 2 || /^\W+$/.test(query)) {
            suggestionsContainer.style.display = 'none';
            return;
        }

        let url = 'https://kakgonbri.zapto.org:8443/prj/ajax/map?action=auto&query=' + encodeURIComponent(query);
        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'OK') {
                    suggestionsContainer.innerHTML = '';
                    suggestionsContainer.style.display = 'block';
                    data.predictions.forEach(prediction => {
                        const div = document.createElement('div');
                        div.className = 'suggestion-item';
                        div.textContent = prediction.description;
                        div.addEventListener('click', () => {
                            addressInput.value = prediction.description;
                            suggestionsContainer.style.display = 'none';
                            updateAllShippingCosts(prediction.description);
                        });
                        suggestionsContainer.appendChild(div);
                    });
                }
            })
            .catch(error => console.error('Error:', error));
    }, 300);

    addressInput.addEventListener('input', (e) => search(e.target.value));
}


function validateAddress() {
    const address = document.getElementById('address').value;
    if (!address) {
        alert('Vui lòng nhập địa chỉ giao hàng');
        return false;
    }
    document.getElementById('hiddenAddress').value = address;
    const shippingCostsInput = document.getElementById('shippingCosts');
    shippingCostsInput.value = JSON.stringify(Object.fromEntries(shippingCosts));
    return true;
}

document.addEventListener('DOMContentLoaded', initializeAddressSearch);

