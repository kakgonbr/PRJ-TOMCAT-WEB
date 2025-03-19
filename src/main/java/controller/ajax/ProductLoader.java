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
        String action = request.getParameter("action"); // Nhận action từ AJAX
        if ("fetchUserShopProducts".equals(action) && user != null) {
            fetchUserShopProducts(response, user);
            return;
        }
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
                        .collect(Collectors.toList()); // let page be 0 for nowa

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

    //fetch product from shop
    private void fetchUserShopProducts(HttpServletResponse response, model.User user) throws IOException {
        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShopByOwnerId(user.getId());

            if (shop == null) {
                response.getWriter().write("[]");
                return;
            }

            java.util.List<model.ProductWrapper> products = dao.ProductDAO.ProductFetcher
                    .getShopProductsByCategory(shop.getId(), 0)
                    .stream().map(model.ProductWrapper::new)
                    .collect(Collectors.toList());

            String json = new com.google.gson.Gson().toJson(products);
            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET SHOP PRODUCTS, REASON: {}", e.getMessage());
            response.getWriter().write("[]");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
