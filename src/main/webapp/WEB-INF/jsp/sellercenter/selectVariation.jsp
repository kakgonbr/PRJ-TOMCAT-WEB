<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericpage title="Select Variation">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/filter_js"></script>
        <script src="${pageContext.request.contextPath}/resources/shop_js"></script>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
            var categoryId = "${sessionScope.categoryId}";

            document.addEventListener("DOMContentLoaded", function () {
                if (!categoryId) {
                    console.error("Category ID is missing in session");
                    return;
                }
                fetchVariations(categoryId);
            });
        </script>            
    </jsp:attribute>

    <jsp:attribute name="header">
        <%-- <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="selectVariation"/> --%>
        <t:userHeader user="${sessionScope.user.username}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Select Variations</h2>
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

        <form id="selectVariationForm" action="${pageContext.request.contextPath}/sellercenter/addproduct" method="post">
            <input type="hidden" name="action" value="selectVariation">

            <label>Choose Variation:</label>
            <div id="variationFilter"></div> 

            <label>Choose Variation Values:</label>
            <div id="variationValueFilter"></div>
            <button type="button" onclick="applyVariation()">Apply Variation</button><br><br>

            <h3>Or Add New Variation</h3>
            <button type="button" onclick="showNewVariationForm()">+ Add New Variation</button>

            <div id="newVariationForm" style="display: none;">
                <label for="variationName">Variation Name:</label>
                <input type="text" id="variationName" name="variation"><br>

                <label for="variationOptions">Options (comma-separated):</label>
                <input type="text" id="variationValues" name="variationValue"><br>
                <label for="datatype">Data Type:</label>
                <select name="datatype" id="datatype">
                    <option value="string">String</option>
                    <option value="integer">Integer</option>
                </select><br>

                <label for="unit">Unit:</label>
                <input type="text" id="unit" name="unit"><br>
                <button type="button" onclick="applyVariation()">Add</button><br><br>
            </div>

            <h3>Selected Variations</h3>
            <table border="1">
                <thead>
                    <tr>
                        <th>Variation Name</th>
                        <th>Datatype</th>
                        <th>Unit</th>
                        <th>Values</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="variationTableBody"></tbody>
            </table>

            <br>
            <button type="button" onclick="submitVariations()">Save Variations</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
