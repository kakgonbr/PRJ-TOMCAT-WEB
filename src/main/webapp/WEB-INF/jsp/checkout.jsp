<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Checkout">
    <jsp:attribute name="head">
        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/checkout_css">
    </jsp:attribute>

    <jsp:attribute name="header">
    
    </jsp:attribute>

    <jsp:attribute name="body">
        <form>
            <div class="address-container">
                <label for="address">Địa chỉ</label>
                <input type="text" id="address" name="address" required placeholder="Nhập địa chỉ của bạn" autocomplete="off">
                <div id="suggestions" class="suggestions"></div>
            </div>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
        <script>
            const addressInput = document.getElementById('address');
            const suggestionsContainer = document.getElementById('suggestions');

            function debounce(func, wait) {
                let timeout;
                return function executedFunc(...args) {
                    clearTimeout(timeout);
                    timeout= setTimeout(() => {clearTimeout(timeout);func(...args);}, wait);
                };
            }

            const search = debounce((query) =>
            {
                if(query.length < 2 || /^\W+$/.test(query)) {
                    suggestionsContainer.style.display= 'none';
                    return;
                }
                
                var url = new URL('https://' + location.host + contextPath + '/ajax/map?action=auto&query=' + encodeURIComponent(query));
                fetch(url.toString)
                .then(response => response.json())
                .then(data => 
                {
                    if(data.status === 'OK') {
                        suggestionsContainer.innerHTML='';
                        suggestionsContainer.style.display= 'block';
                        data.predictions.forEach(prediction => 
                        {
                            const div = document.createElement('div');
                            div.className = 'suggestion-item';
                            div.textContent = prediction.description;
                            div.addEventListener('click', () => 
                            {
                                addressInput.value = prediction.description;
                                suggestionsContainer.style.display = 'none';
                            });
                            suggestionsContainer.appendChild(div);
                        });
                    }
                })  
                .catch(error => console.error('Error:', error));
            },300);

            addressInput.addEventListener('input', (e) => search(e.target.value));
        </script>
    </jsp:attribute>
</t:genericpage>