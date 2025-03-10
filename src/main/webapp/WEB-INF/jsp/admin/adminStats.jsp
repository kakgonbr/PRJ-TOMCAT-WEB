<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage title="Admin Statistics">
    <jsp:attribute name="head">
        <t:resources/>

        <script>
            var contextPath = "${pageContext.request.contextPath}";
        </script>
        

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="${pageContext.request.contextPath}/resources/chart_js"></script>
        <script src="${pageContext.request.contextPath}/resources/admin_js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin_css">
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>Admin page.</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>admin?</p>
        <form action="${pageContext.request.contextPath}/admin" method="POST">
            <input type="hidden" name="action" value="enableMaintenance"/>
            <input type="submit" value="Enable Maintenance"/>
        </form>
        <form action="${pageContext.request.contextPath}/admin" method="POST">
            <input type="hidden" name="action" value="disableMaintenance"/>
            <input type="submit" value="Disable Maintenance"/>
        </form>
        <form action="${pageContext.request.contextPath}/admin" method="POST">
            <input type="hidden" name="action" value="logStatistics"/>
            <input type="submit" value="Log today's statistics"/>
        </form>

        <canvas id="chartTotalMoney" class="adminChart"></canvas>
        <canvas id="chartNumVisit" class="adminChart"></canvas>
        <canvas id="chartNumPurchase" class="adminChart"></canvas>
        <canvas id="chartNumUsers" class="adminChart"></canvas>
        <canvas id="chartNumProducts" class="adminChart"></canvas>
        <canvas id="chartNumShops" class="adminChart"></canvas>
        <canvas id="chartNumPromotions" class="adminChart"></canvas>
        <pre id="logContainer"></pre>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>