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

        if (action != null && productId != null) {
            try {
                int id = Integer.parseInt(productId);

                if (action.equals("edit")) {
                    try {
                        request.setAttribute("product", new model.ProductDetailsWrapper(dao.ProductDAO.ProductFetcher.getProductDetails(id)));
                        request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                    } catch (Exception e) {
                        service.Logging.logger.error("Error processing edit action: ", e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
                    }
                } else if (action.equals("delete")) {
                    dao.ProductDAO.ProductManager.deleteProduct(id);

                    if (true) {
                        response.sendRedirect("/shophome");
                        return;
                    } else {
                        request.setAttribute("error", "Delete failed");
                        request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
                    }
                    return;
                }
            } catch (java.sql.SQLException | NumberFormatException e) {
                service.Logging.logger.warn("FAILED TO PROCESS ACTION {} FOR PRODUCT ID {}, REASON: {}", action, productId, e.getMessage());
                request.setAttribute("error", "true");
                return;
            }
        } else if (productId != null) {
            request.getRequestDispatcher(config.Config.JSPMapper.PRODUCT_DETAILS).forward(request, response);
        }

        try {
            request.setAttribute("product", new model.ProductDetailsWrapper(dao.ProductDAO.ProductFetcher.getProductDetails(Integer.parseInt(productId))));
        } catch (java.sql.SQLException | NumberFormatException e) {
            service.Logging.logger.warn("FAILED TO GET PRODUCT INFORMATION FOR PRODUCT ID {}, REASON: {}", productId, e.getMessage());

            request.setAttribute("error", "true");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopIdValue = (Integer) session.getAttribute("shopId");
        String productName = request.getParameter("name");
        String categoryParam = request.getParameter("category");
        String description = request.getParameter("description");
        String productItemIdParam = request.getParameter("productItemId");
        String stockParam = request.getParameter("productItemStock");
        String priceParam = request.getParameter("productItemPrice");

        if (productItemIdParam == null || stockParam == null || priceParam == null) {
            request.setAttribute("error", "Missing parameters.");
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
            return;
        }
        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            // Lấy đối tượng Category từ ID
            int categoryIdInt = Integer.parseInt(categoryParam);
            model.Category category = dao.CategoryDAO.CategoryFetcher.getCategoryDetails(categoryIdInt);
            if (category == null) {
                request.setAttribute("error", "invalid_category");
                request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
                return;
            }
            // save product to db
            Product product = new Product();
            product.setShopId(shop);
            product.setCategoryId(category);
            product.setName(productName);
            product.setDescription(description);
            product.setImageStringResourceId(null);
            product.setStatus(true);
            dao.ProductDAO.ProductManager.editProduct(product);
            int productItemId = Integer.parseInt(productItemIdParam);
            int stock = Integer.parseInt(stockParam);
            BigDecimal price = new BigDecimal(priceParam);

            dao.ProductItemDAO.productItemManager.editProductItem(productItemId, stock, price);

            response.sendRedirect(request.getContextPath() + "/shophome");
            System.out.println("Updated productItem: ID=" + productItemId + ", Stock=" + stock + ", Price=" + price);
        } catch (NumberFormatException e) {
            service.Logging.logger.warn("Invalid input format: {}", e.getMessage());
            request.setAttribute("error", "Invalid number format.");
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Database error while updating product item: {}", e.getMessage());
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }
    }
}
