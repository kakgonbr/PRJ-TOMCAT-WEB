package controller.ajax;

import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProductLoader extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.User user = (model.User) session.getAttribute("user");
        String recommendations = (String) request.getAttribute("query");

        if (recommendations == null || recommendations.isBlank()) {
            recommendations = "";
        }

        try {
            java.util.List<model.ProductWrapper> products = dao.ProductDAO.ProductFetcher.getRecommendation(recommendations).stream().map(model.ProductWrapper::new).collect(Collectors.toList());

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
        doGet(request, response);
    }
}
