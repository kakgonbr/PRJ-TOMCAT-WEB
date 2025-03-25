<%-- POSSIBLE REQUEST ATTRIBUTES:  --%>
<%-- POSSIBLE REQUEST PARAMETERS:  --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <t:error error="${error}" />
    </jsp:attribute>

    <jsp:attribute name="body">
        <div class="my-3 container">
            <div class="row justify-content-center">
                <div class="col-lg-8 text-center">
                    <p class="lead text-muted"><a class="btn shadow custom-outline-button" href="${pageContext.request.contextPath}/admin/cp">To Control Panel</a></p>
                    <h2 class="fw-bold mb-3">Current Statistics</h2>
                </div>
            </div>
        </div>
        <div class="my-5 container">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="row row-cols-1 row-cols-md-3 g-4 rounded shadow-lg">
                        <div class="col border-end">
                            <div class="text-center p-4">
                                <h5 class="text-muted mb-0">Average Response Time</h5>
                                <c:choose>
                                    <c:when test="${averageResponse < 100}">
                                        <h1 class="fw-bold text-success">${averageResponse}</h1>
                                    </c:when>
                                    <c:when test="${averageResponse < 200}">
                                        <h1 class="fw-bold text-warning">${averageResponse}</h1>
                                    </c:when>
                                    <c:otherwise>
                                        <h1 class="fw-bold text-danger">${averageResponse}</h1>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col border-end">
                            <div class="text-center p-4">
                                <h5 class="text-muted mb-0">Max Response Time</h5>
                                <c:choose>
                                    <c:when test="${maxResponse < 250}">
                                        <h1 class="fw-bold text-success">${maxResponse}</h1>
                                    </c:when>
                                    <c:when test="${maxResponse < 500}">
                                        <h1 class="fw-bold text-warning">${maxResponse}</h1>
                                    </c:when>
                                    <c:otherwise>
                                        <h1 class="fw-bold text-danger">${maxResponse}</h1>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col">
                            <div class="text-center p-4">
                                <h5 class="text-muted mb-0">Current Active Sessions</h5>
                                <h1 class="fw-bold">${currentSessions}</h1>
                            </div>
                        </div>
                        <div class="col border-end">
                            <div class="text-center p-4">
                                <h5 class="text-muted mb-0">Peak Number of Sessions</h5>
                                <h1 class="fw-bold">${peakSessions}</h1>
                            </div>
                        </div>
                        <div class="col border-end">
                            <div class="text-center p-4">
                                <h5 class="text-muted mb-0">Number of Visits</h5>
                                <h1 class="fw-bold">${visits}</h1>
                            </div>
                        </div>
                        <div class="col">
                            <div class="text-center p-4">
                                <h5 class="text-muted mb-0">Log statistics</h5>
                                <h1 class="fw-bold"><form action="${pageContext.request.contextPath}/admin" method="POST">
                                    <input type="hidden" name="action" value="logStatistics" />
                                    <input type="submit" value="Log today's statistics" class="btn shadow custom-outline-button"/>
                                </form></h1>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

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

        <div class="container p-3 my-2" style="background-color: var(--bs-custom-container-focus);">
            <pre id="logContainer">Loading logs</pre>
        </div>
    </jsp:attribute>

    <jsp:attribute name="footer">
        <t:footer/>
    </jsp:attribute>
</t:genericpage>

<%-- POSSIBLE DESTINATIONS:  --%>