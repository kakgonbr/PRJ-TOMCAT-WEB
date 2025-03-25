package controller.ajax;

import java.io.IOException;
import java.io.PrintWriter;
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

        PrintWriter out = response.getWriter();

        try {
            String json = new Gson().toJson(service.AdminService.StatsService.createCharts());
            service.Logging.logger.info("Sending back to admin json: {}", json);
        
            out.print(json);
            out.flush();
            
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO GET SERVER STATISTICS, REASON: {}", e.getMessage());

            return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
