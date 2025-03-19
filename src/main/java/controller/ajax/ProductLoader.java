package controller.ajax;

import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProductLoader extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // specific product
        response.setContentType("application/json");

        String productId = request.getParameter("productId");
        if (productId != null && !productId.isBlank()) {
            try {
                String json = new com.google.gson.Gson().toJson(new model.ProductDetailsWrapper(dao.ProductDAO.ProductFetcher.getProductDetails(Integer.parseInt(productId))));

                service.Logging.logger.info("Sending back json {}", json);

                response.getWriter().write(json);
                return;
            } catch (java.sql.SQLException | NumberFormatException e) {
                service.Logging.logger.warn("FAILED TO GET PRODUCT DETAILS FOR PRODUCT ID {}, REASON: {}", productId, e.getMessage());
                return;
            }
        }

        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");
        String recommendations = request.getParameter("query");
        Integer category = request.getParameter("category") == null || request.getParameter("category").isBlank() ? 0 : Integer.parseInt(request.getParameter("category"));
        String shopId = request.getParameter("shopId");

        try {
            if (recommendations == null || recommendations.isBlank()) {
                recommendations = "";

                if (user != null) {
                    // try to get the user's preference here
                }
            }

            java.util.List<model.ProductWrapper> products;

            if (shopId == null || shopId.isBlank()) {
                service.Logging.logger.info("Getting recommendations for query '{}'", recommendations);

                products = dao.ProductDAO.ProductFetcher
                        .getRecommendation(recommendations, 0, category).stream().map(model.ProductWrapper::new)
                        .collect(Collectors.toList()); // let page be 0 for now

            } else {
                service.Logging.logger.info("Getting shop products for shop {}", shopId);

                products = dao.ProductDAO.ProductFetcher.getShopProductsByCategory(Integer.parseInt(shopId), category).stream().map(model.ProductWrapper::new).collect(Collectors.toList());
            }

            String json = new com.google.gson.Gson().toJson(products);

            service.Logging.logger.info("Sending back json {}", json);

            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET RECOMMENDATIONS, REASON: {}", e.getMessage());

            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
