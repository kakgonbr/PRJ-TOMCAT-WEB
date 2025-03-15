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
        <script src="${pageContext.request.contextPath}/resources/log_js"></script>
        
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/admin_css">
    </jsp:attribute>

    <jsp:attribute name="header">
        <h1>Admin page.</h1>
    </jsp:attribute>

    <jsp:attribute name="body">
        <p>admin?</p>
        <div class="chart-container">
            <div class="chart-item">
                <canvas id="chartTotalMoney" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartNumVisit" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartNumPurchase" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartNumUsers" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartNumProducts" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartNumShops" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartNumPromotions" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartAvgResponse" class="adminChart"></canvas>
            </div>
            <div class="chart-item">
                <canvas id="chartMaxResponse" class="adminChart"></canvas>
            </div>
        </div>

        <pre id="logContainer"></pre>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>