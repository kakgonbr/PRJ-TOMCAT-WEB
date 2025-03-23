package controller.ajax;

import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class VariationLoader extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        // Log toàn bộ request
        service.Logging.logger.info("Received request: {}", request.getRequestURI());
        service.Logging.logger.info("Query Parameters: categoryId={}, variationId={}", 
                                    request.getParameter("categoryId"), 
                                    request.getParameter("variationId"));

        String categoryId = request.getParameter("categoryId");
        String variationId = request.getParameter("variationId");

        try {
            if (categoryId != null && !categoryId.isBlank()) {
                service.Logging.logger.info("Fetching variations for category ID {}", categoryId);

                java.util.List<model.VariationWrapper> variations = dao.VariationDAO.VariationFetcher
                        .getVariationsByCategoryId(Integer.parseInt(categoryId))
                        .stream()
                        .map(model.VariationWrapper::new)
                        .collect(Collectors.toList());

                String json = new com.google.gson.Gson().toJson(variations);

                service.Logging.logger.info("Sending back JSON: {}", json);

                response.getWriter().write(json);
                return;
            }

            if (variationId != null && !variationId.isBlank()) {
                service.Logging.logger.info("Fetching variation values for variation ID {}", variationId);

                java.util.List<model.VariationValueWrapper> variationValues = dao.VariationValueDAO.VariationValueFetcher
                        .getVariationValuesByVariationId(Integer.parseInt(variationId))
                        .stream()
                        .map(model.VariationValueWrapper::new)
                        .collect(Collectors.toList());

                String json = new com.google.gson.Gson().toJson(variationValues);

                service.Logging.logger.info("Sending back JSON: {}", json);

                response.getWriter().write(json);
                return;
            }

            service.Logging.logger.warn("No valid parameters provided for fetching variations");

        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO FETCH VARIATIONS, REASON: {}", e.getMessage(), e);
        } catch (NumberFormatException e) {
            service.Logging.logger.error("Invalid number format for categoryId or variationId: {}", e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
