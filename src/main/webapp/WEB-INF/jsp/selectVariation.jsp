<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<t:genericpage title="Select Variation">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var shopId = "${sessionScope.shopId}";

            document.addEventListener("DOMContentLoaded", function () {
                fetchVariation();
            });

            function toggleNewVariationForm() {
                let form = document.getElementById("newVariationForm");
                form.style.display = form.style.display === "none" ? "block" : "none";
            }

            // Hiển thị Variation đã chọn
            function updateSelectedVariations() {
                let container = document.getElementById("selectedVariations");
                container.innerHTML = "";

                document.querySelectorAll("input[name='variation']:checked").forEach(variation => {
                    let div = document.createElement("div");
                    div.innerHTML = <strong>${variation.dataset.name}</strong>;
                    
                    let ul = document.createElement("ul");
                    document.querySelectorAll(input[name='variationValue'][data-parent='${variation.value}']:checked).forEach(value => {
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

        <form id="selectVariationForm" action="${pageContext.request.contextPath}/addproduct" method="post">
            <input type="hidden" name="action" value="selectVariation">

            <label>Choose Variation:</label>
            <div id="variationFilter"></div> 

            <button type="button" onclick="toggleNewVariationForm()">Add New Variation</button><br>

            <div id="newVariationForm" style="display: none;">
                <label for="variationName">Variation Name:</label>
                <input type="text" id="variationName" name="variationName"><br>

                <label for="variationOptions">Options (comma-separated):</label>
                <input type="text" id="variationOptions" name="variationOptions"><br>
            </div>

            <h3>Selected Variations:</h3>
            <div id="selectedVariations"></div>

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
