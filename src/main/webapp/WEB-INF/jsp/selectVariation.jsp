<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Select Variations">
    <jsp:attribute name="head">
        <t:resources/>
        <script src="${pageContext.request.contextPath}/resources/variation"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                initVariationTable();
            });
        </script>
    </jsp:attribute>

    <jsp:attribute name="header">
        <t:nav mainNav="true" user="${sessionScope.user.username}" activePage="selectVariation"/>
    </jsp:attribute>

    <jsp:attribute name="body">
        <h2>Select Variations</h2>
        <form id="selectVariationForm">
            <label for="variationName">Variation Name:</label>
            <input type="text" id="variationName" name="variationName" required><br>

            <label for="variationOptions">Options (comma-separated):</label>
            <input type="text" id="variationOptions" name="variationOptions" required><br>

            <button type="button" onclick="addVariation()">Add Variation</button><br>

            <table>
                <thead>
                    <tr id="variationHeader"></tr>
                </thead>
                <tbody id="variationBody"></tbody>
            </table>

            <button type="submit">Save Variations</button>
        </form>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>
