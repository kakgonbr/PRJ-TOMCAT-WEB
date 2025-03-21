package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import model.Product;

public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String productId = request.getParameter("productId");

        if ("edit".equals(action) && productId != null) {
            try {
                int id = Integer.parseInt(productId);
                request.setAttribute("product", new model.ProductDetailsWrapper(dao.ProductDAO.ProductFetcher.getProductDetails(id)));
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
            } catch (Exception e) {
                service.Logging.logger.error("Error processing edit action: ", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            handleEditProduct(request, response);
        } else if ("delete".equals(action)) {
            handleDeleteProduct(request, response);
        }
    }

    private void handleEditProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopIdValue = (Integer) session.getAttribute("shopId");
        String productIdParam = request.getParameter("productId");
        String productName = request.getParameter("name");
        String categoryParam = request.getParameter("filter");
        String description = request.getParameter("description");
        String[] productItemIds = request.getParameterValues("productItemId");
        String[] stocks = request.getParameterValues("stock");
        String[] prices = request.getParameterValues("price");

        if (productItemIds == null || stocks == null || prices == null) {
            request.setAttribute("error", "Missing or invalid parameters.");
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            int categoryIdInt = Integer.parseInt(categoryParam);
            model.Category category = dao.CategoryDAO.CategoryFetcher.getCategoryDetails(categoryIdInt);
            if (category == null) {
                request.setAttribute("error", "invalid_category");
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                return;
            }

            Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);
            if (product == null) {
                request.setAttribute("error", "Product not found.");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            java.util.List<model.ProductItem> productItemList = new java.util.ArrayList<>();
            product.setShopId(shop);
            product.setCategoryId(category);
            product.setName(productName);
            product.setDescription(description);
            product.setImageStringResourceId(null);
            product.setStatus(true);
            dao.ProductDAO.ProductManager.editProduct(product);

            for (int i = 0; i < productItemIds.length; i++) {
                int productItemId = Integer.parseInt(productItemIds[i]);
                int stock = Integer.parseInt(stocks[i]);
                BigDecimal price = new BigDecimal(prices[i]);

                model.ProductItem item = new model.ProductItem();
                item.setId(productItemId);
                item.setStock(stock);
                item.setPrice(price);
                productItemList.add(item);
            }

            dao.ProductDAO.ProductManager.updateMultipleProductItems(productId, productItemList);
            response.sendRedirect(request.getContextPath() + "/shophome");
        } catch (NumberFormatException e) {
            service.Logging.logger.warn("Invalid input format: {}", e.getMessage());
            request.setAttribute("error", "Invalid number format." + e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Database error while updating product item: {}", e.getMessage());
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }
    }

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productIdParam = request.getParameter("productId");
        try {
            int productId = Integer.parseInt(productIdParam);
            dao.ProductDAO.ProductManager.deleteProduct(productId);
            response.sendRedirect(request.getContextPath() + "/shophome");
        } catch (NumberFormatException e) {
            service.Logging.logger.warn("Invalid product ID format: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Database error while deleting product: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}
