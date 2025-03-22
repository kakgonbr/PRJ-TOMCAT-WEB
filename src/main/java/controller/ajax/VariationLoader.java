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

        String categoryId = request.getParameter("categoryId");
        String variationId = request.getParameter("variationId");

        try {
            if (categoryId != null && !categoryId.isBlank()) {
                service.Logging.logger.info("Fetching variations for category ID {}", categoryId);
                
                java.util.List<model.Variation> variations = dao.VariationDAO.VariationFetcher.getVariationsByCategoryId(Integer.parseInt(categoryId));
                
                // Ensure lazy-loaded collections are initialized
                for (model.Variation v : variations) {
                    v.getVariationValueList().size();
                }
                
                java.util.List<model.VariationWrapper> variationWrappers = variations.stream()
                        .map(model.VariationWrapper::new)
                        .collect(Collectors.toList());
                
                String json = new com.google.gson.Gson().toJson(variationWrappers);
                service.Logging.logger.info("Sending back JSON {}", json);
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

                service.Logging.logger.info("Sending back JSON {}", json);

                response.getWriter().write(json);
                return;
            }

            service.Logging.logger.warn("No valid parameters provided for fetching variations");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO FETCH VARIATIONS, REASON: {}", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
