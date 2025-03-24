package controller;

import java.io.IOException;
import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1
    maxFileSize = 1024 * 1024 * 10, // 10
    maxRequestSize = 1024 * 1024 * 50 // 50
)
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");

        if (productId != null) {
            int id = Integer.parseInt(productId);

            if ("edit".equals(request.getParameter("action"))) {
                try {
                    request.setAttribute("product",
                            new model.ProductDetailsWrapper(dao.ProductDAO.ProductFetcher.getProductDetails(id)));
                    request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                } catch (Exception e) {
                    service.Logging.logger.error("Error processing edit action: {}", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
                }
                return;
            }
            request.getRequestDispatcher(config.Config.JSPMapper.PRODUCT_INFO).forward(request, response);

            return;
        }

        request.setAttribute("code", 404);
        request.getRequestDispatcher("/error").forward(request, response);

        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            handleDeleteProduct(request, response);
        } else if ("edit".equals(action)) {
            handleEditProduct(request, response);
        } else if ("restore".equals(action)) {
            handleRestoreProduct(request, response);
        } else if ("addPromotion".equals(action)) {
            addPromotion(request, response);
        } else if ("removePromotion".equals(action)) {
            removePromotion(request, response);
        } else if ("setThumbnail".equals(action)) {
            setThumbnail(request, response);
        } else if ("addPicture".equals(action)) {
            addPicture(request, response);
        } else if ("removePicture".equals(action)) {
            removePicture(request, response);
        }
    }

    private void removePicture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer shopIdValue = (Integer) request.getSession().getAttribute("shopId");
        Integer productId = Integer.parseInt(request.getParameter("productId"));
        String imageId = request.getParameter("imageId");

        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            model.Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);

            if (!product.getShopId().equals(shop)) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            product.getProductImageList().removeIf(pi -> pi.getImageStringResourceId().getId().equals(imageId));

            dao.ProductDAO.ProductManager.editProduct(product);

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to save thumbnail, reason: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }
    }

    private void addPicture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer shopIdValue = (Integer) request.getSession().getAttribute("shopId");
        Integer productId = Integer.parseInt(request.getParameter("productId"));

        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            model.Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);

            if (!product.getShopId().equals(shop)) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            String saved = service.FileUploadService.saveFile(request.getPart("image"), productId.toString());

            model.ProductImage productImage = new model.ProductImage();
            productImage.setProductId(product);
            productImage.setImageStringResourceId(dao.ResourceDAO.overrideMapping("prd_" + product.getId() + product.getProductImageList().size(), saved));

            product.getProductImageList().add(productImage); // this feels quite C++ actually

            dao.ProductDAO.ProductManager.editProduct(product);

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to save thumbnail, reason: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }
    }

    private void setThumbnail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer shopIdValue = (Integer) request.getSession().getAttribute("shopId");
        Integer productId = Integer.parseInt(request.getParameter("productId"));

        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            model.Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);

            if (!product.getShopId().equals(shop)) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            String saved = service.FileUploadService.saveFile(request.getPart("image"), productId.toString());

            product.setImageStringResourceId(dao.ResourceDAO.overrideMapping("prd_" + product.getId(), saved));
            dao.ProductDAO.ProductManager.editProduct(product);

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to save thumbnail, reason: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }
    }

    private void removePromotion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer shopIdValue = (Integer) request.getSession().getAttribute("shopId");
        Integer productId = Integer.parseInt(request.getParameter("productId"));

        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            model.Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);

            if (!product.getShopId().equals(shop)) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            product.setAvailablePromotionId(null);

            dao.ProductDAO.ProductManager.editProduct(product);
            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to remove promotion, reason: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }

    }

    private void addPromotion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer shopIdValue = (Integer) request.getSession().getAttribute("shopId");
        String name = request.getParameter("name");
        Boolean type = Boolean.parseBoolean(request.getParameter("type")); // 1 is flat 0 is %
        Integer value = Integer.parseInt(request.getParameter("value"));
        String expire = request.getParameter("expire");
        Integer productId = Integer.parseInt(request.getParameter("productId"));

        try {
            model.Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopIdValue);
            if (shop == null) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            model.Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);

            if (!product.getShopId().equals(shop)) {
                request.setAttribute("error", "invalid_shop");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }
            
            if (product.getAvailablePromotionId() != null && product.getAvailablePromotionId().getStatus()) {
                request.setAttribute("error", "Product already has promotion.");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            // not sure if this works
            if (type && value > product.getProductItemList().stream().min((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())).get().getPrice().longValue() / 2 || value < 1) {
                request.setAttribute("error", "Invalid value.");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            if (!type && value > 99 || value < 1) {
                request.setAttribute("error", "Invalid value.");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            java.sql.Date expireDate = java.sql.Date.valueOf(java.time.LocalDate.parse(expire, config.Config.Time.inputFormatDate));

            if (expireDate.before(java.sql.Date.valueOf(java.time.LocalDate.now()))) {
                request.setAttribute("error", "Invalid expire date.");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
                return;
            }

            model.Promotion promotion = new model.Promotion();
            promotion.setCreationDate(java.sql.Date.valueOf(java.time.LocalDate.now()));
            promotion.setExpireDate(expireDate);
            promotion.setType(type);
            promotion.setCreatorId((model.User) request.getSession().getAttribute("user"));
            promotion.setName(name);
            promotion.setOfAdmin(false);
            promotion.setStatus(true);

            promotion = dao.PromotionDAO.PromotionManager.addPromotion(promotion);

            product.setAvailablePromotionId(promotion);

            dao.ProductDAO.ProductManager.editProduct(product);

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to add promotion, reason: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);
        }

    }

    private void handleEditProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopIdValue = (Integer) session.getAttribute("shopId");
        String productIdParam = request.getParameter("id");
        String productName = request.getParameter("name");
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

            Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);
            if (product == null) {
                request.setAttribute("error", "Product not found.");
                request.getRequestDispatcher(config.Config.JSPMapper.EDIT_PRODUCT).forward(request, response);

                return;
            }

            java.util.List<model.ProductItem> productItemList = new java.util.ArrayList<>();
            product.setShopId(shop);
            // product.setCategoryId(category);
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
            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");

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

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String productIdParam = request.getParameter("productId");
        try {
            int productId = Integer.parseInt(productIdParam);
            dao.ProductDAO.ProductManager.deleteProduct(productId);
            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "number format occur");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        } catch (java.sql.SQLException e) {
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        }
    }

    // handle restore product
    private void handleRestoreProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String productIdParam = request.getParameter("productId");
        try {
            int productId = Integer.parseInt(productIdParam);

            Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);
            if (product != null) {
                product.setStatus(true);
                dao.ProductDAO.ProductManager.editProduct(product);
            }

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID format.");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        } catch (java.sql.SQLException e) {
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        }
    }
}
