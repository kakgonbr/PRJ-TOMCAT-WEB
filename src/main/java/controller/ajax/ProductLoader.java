package controller.ajax;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProductLoader extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.User user = (model.User) session.getAttribute("user");
        String recommendations = (String) request.getAttribute("query");

        if (recommendations == null || recommendations.isBlank()) return;

        try {
            java.util.List<model.Product> products = dao.ProductDAO.ProductFetcher.getRecommendation(service.DatabaseConnection.getConnection(), recommendations);

            response.setContentType("application/json");

            String json = new com.google.gson.Gson().toJson(products);

            service.Logging.logger.info("Sending back to user {} json {}", user.getId(), json);

            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET RECOMMENDATIONS FOR {}, REASON: {}", user.getId(), e.getMessage());

            return;
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
