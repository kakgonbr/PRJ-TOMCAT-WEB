document.addEventListener('DOMContentLoaded', () => {
    const addressInput = document.getElementById('address');
    const suggestionsContainer = document.getElementById('suggestions');

    function debounce(func, wait) {
        let timeout;
        return function executedFunc(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => { clearTimeout(timeout); func(...args); }, wait);
        };
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
                        });
                        suggestionsContainer.appendChild(div);
                    });
                }
            })
            .catch(error => console.error('Error:', error));
    }, 300);

    addressInput.addEventListener('input', (e) => search(e.target.value));
});

function validateAddress() {
    const address = document.getElementById('address').value;
    if (!address) {
        alert('Vui lòng nhập địa chỉ giao hàng');
        return false;
    }
    document.getElementById('hiddenAddress').value = address;
    return true;
}