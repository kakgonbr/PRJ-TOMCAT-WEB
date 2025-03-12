package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminStatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        java.util.List<model.TblServerStatistics> statistics;

        try {
            statistics = dao.StatisticsDAO.SystemStatisticsManager.getStatistics();
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO GET SERVER STATISTICS, REASON: {}", e.getMessage());

            return;
        }

        java.util.List<String> dates = statistics.stream().map(model.TblServerStatistics::getDay).map(misc.Utils::formatDate).collect(Collectors.toList());

        // this is really, really long
        Map<String, Object> chartTotalMoney = new HashMap<>();
        chartTotalMoney.put("for", "chartTotalMoney");
        chartTotalMoney.put("type", "line");
        chartTotalMoney.put("label", "Money Earned");
        chartTotalMoney.put("labels", dates);
        chartTotalMoney.put("values", statistics.stream().map(model.TblServerStatistics::getTotalMoneyEarned).collect(Collectors.toList()));

        Map<String, Object> chartNumVisit = new HashMap<>();
        chartNumVisit.put("for", "chartNumVisit");
        chartNumVisit.put("type", "line");
        chartNumVisit.put("label", "Number of Visits");
        chartNumVisit.put("labels", dates);
        chartNumVisit.put("values", statistics.stream().map(model.TblServerStatistics::getVisitNum).collect(Collectors.toList()));

        Map<String, Object> chartNumPurchase = new HashMap<>();
        chartNumPurchase.put("for", "chartNumPurchase");
        chartNumPurchase.put("type", "line");
        chartNumPurchase.put("label", "Number of Purchases");
        chartNumPurchase.put("labels", dates);
        chartNumPurchase.put("values", statistics.stream().map(model.TblServerStatistics::getPurchaseNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumUsers = new HashMap<>();
        chartNumUsers.put("for", "chartNumUsers");
        chartNumUsers.put("type", "line");
        chartNumUsers.put("label", "Number of Users");
        chartNumUsers.put("labels", dates);
        chartNumUsers.put("values", statistics.stream().map(model.TblServerStatistics::getUserNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumProducts = new HashMap<>();
        chartNumProducts.put("for", "chartNumProducts");
        chartNumProducts.put("type", "line");
        chartNumProducts.put("label", "Number of Products");
        chartNumProducts.put("labels", dates);
        chartNumProducts.put("values", statistics.stream().map(model.TblServerStatistics::getProductNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumShops = new HashMap<>();
        chartNumShops.put("for", "chartNumShops");
        chartNumShops.put("type", "line");
        chartNumShops.put("label", "Number of Shops");
        chartNumShops.put("labels", dates);
        chartNumShops.put("values", statistics.stream().map(model.TblServerStatistics::getShopNum).collect(Collectors.toList()));
        
        Map<String, Object> chartNumPromotions = new HashMap<>();
        chartNumPromotions.put("for", "chartNumPromotions");
        chartNumPromotions.put("type", "line");
        chartNumPromotions.put("label", "Number of promotions");
        chartNumPromotions.put("labels", dates);
        chartNumPromotions.put("values", statistics.stream().map(model.TblServerStatistics::getPromotionNum).collect(Collectors.toList()));

        Map<String, Object> chartAvgResponse = new HashMap<>();
        chartAvgResponse.put("for", "chartAvgResponse");
        chartAvgResponse.put("type", "line");
        chartAvgResponse.put("label", "Average request response time (ms)");
        chartAvgResponse.put("labels", dates);
        chartAvgResponse.put("values", statistics.stream().map(model.TblServerStatistics::getPromotionNum).collect(Collectors.toList()));

        Map<String, Object> chartMaxResponse = new HashMap<>();
        chartMaxResponse.put("for", "chartMaxResponse");
        chartMaxResponse.put("type", "line");
        chartMaxResponse.put("label", "Maximum request response time (ms)");
        chartMaxResponse.put("labels", dates);
        chartMaxResponse.put("values", statistics.stream().map(model.TblServerStatistics::getPromotionNum).collect(Collectors.toList()));
        
        java.util.List<Map<String, Object>> charts = java.util.Arrays.asList(chartTotalMoney, chartNumVisit, chartNumPurchase, chartNumUsers, chartNumProducts, chartNumShops, chartNumPromotions, chartAvgResponse, chartMaxResponse);

        PrintWriter out = response.getWriter();

        String json = new Gson().toJson(charts);

        service.Logging.logger.info("Sending back to admin json: {}", json);

        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
