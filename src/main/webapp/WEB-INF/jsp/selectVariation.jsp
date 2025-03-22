<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Select Variation">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var categoryId = "${sessionScope.categoryId}";
            var variationId = null;

            document.addEventListener("DOMContentLoaded", function () {
                if (!categoryId) {
                    console.error("Category ID is missing in session");
                    return;
                }
                fetchVariations(categoryId);
                fetchVariationValues(variationId);
            });

            function toggleNewVariationForm() {
                let form = document.getElementById("newVariationForm");
                form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
            }

            function updateSelectedVariations() {
                let container = document.getElementById("selectedVariations");
                container.innerHTML = "";

                document.querySelectorAll("input[name='variation']:checked").forEach(variation => {
                    let div = document.createElement("div");
                    div.innerHTML = `<strong>${variation.dataset.name}</strong>`;

                    let ul = document.createElement("ul");
                    document.querySelectorAll(`input[name="variationValue"][data-parent="${variation.value}"]:checked`).forEach(value => {
                        let li = document.createElement("li");
                        li.textContent = value.dataset.name;
                        ul.appendChild(li);
                    });

                    div.appendChild(ul);
                    container.appendChild(div);
                });
            }
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="selectVariation"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Select Variations</h2>
        <p>Debug: Category ID from session = ${sessionScope.categoryId}</p>
        <c:if test="${not empty error}">
            <div style="color: red; font-weight: bold; padding: 10px; border: 1px solid red; background-color: #ffe6e6;">
                <c:choose>
                    <c:when test="${error == 'missing_fields'}">
                        ⚠️ Please fill in all required fields.
                    </c:when>
                    <c:when test="${error == 'variation_creation_failed'}">
                        ❌ Failed to create variation. Please try again.
                    </c:when>
                    <c:when test="${error == 'db_error'}">
                        ❗ A database error occurred. Please contact support.
                    </c:when>
                    <c:otherwise>
                        🚨 Unknown error occurred.
                    </c:otherwise>
                </c:choose>
                <br>
                <c:if test="${not empty errorMessage}">
                    <strong>Details:</strong> ${errorMessage}
                </c:if>
            </div>
        </c:if>

        <form id="selectVariationForm" action="${pageContext.request.contextPath}/addproduct" method="post">
            <input type="hidden" name="action" value="selectVariation">

            <label>Choose Variation:</label>
            <div id="variationFilter"></div> 
            
             <button type="button" onclick="fetchVariationValues()" class="btn btn-warning">Apply Variation</button>
             
            <label>Choose Variation Values:</label>
            <div id="variationValueFilter"></div> 

            <button type="button" onclick="toggleNewVariationForm()">Add New Variation</button><br>

            <div id="newVariationForm" style="display: none;">
                <label for="variationName">Variation Name:</label>
                <input type="text" id="variationName" name="variationName"><br>

                <label for="variationOptions">Options (comma-separated):</label>
                <input type="text" id="variationOptions" name="variationOptions"><br>
            </div>

            <label for="datatype">Data Type:</label>
            <select name="datatype" id="datatype">
                <option value="string">String</option>
                <option value="integer">Integer</option>
            </select><br>

            <label for="unit">Unit:</label>
            <input type="text" id="unit" name="unit"><br>

            <button type="submit">Save Variations</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
