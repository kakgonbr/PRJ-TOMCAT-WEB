package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Promotion;
import model.Product;
import model.User;

public class PromotionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_ADD_PROMOTION).forward(request, response);
            return;
        }

        if ("products".equals(action)) {
            handlePromotionProducts(request, response, session);
            return;
        }

        showPromotionList(request, response, user);
    }

    private void handlePromotionProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        String promotionIdStr = request.getParameter("promotionId");
        Integer shopId = (Integer) session.getAttribute("shopId");

        if (promotionIdStr == null || shopId == null) {
            response.sendRedirect(request.getContextPath() + "/sellercenter/promotion");
            return;
        }

        try {
            int promotionId = Integer.parseInt(promotionIdStr);
            List<Product> products = dao.ProductDAO.ProductFetcher.getShopProductsByPromotion(shopId, promotionId);
            request.setAttribute("products", products);
            List<Product> unpromotedProducts = dao.ProductDAO.ProductFetcher.getShopProductsWithoutPromotion(shopId);
            request.setAttribute("unpromotedProducts", unpromotedProducts);

            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_PROMOTION_DETAILS).forward(request, response);
        } catch (NumberFormatException e) {
            service.Logging.logger.error("Invalid promotionId format: {}", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid promotionId format.");
        } catch (Exception e) {
            service.Logging.logger.error("Error fetching products: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

    private void showPromotionList(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int userId = user.getId();
        try {
            List<Promotion> promotions = dao.PromotionDAO.PromotionFetcher.checkAvailablePromotionsByCreator(userId);
            request.setAttribute("promotions", promotions);
            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_DISPLAY_PROMOTION).forward(request, response);
        } catch (Exception e) {
            service.Logging.logger.error("Error fetching promotions: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("addPromotion".equals(action)) {
            addPromotion(request, response);
        } else if ("addProductPromotion".equals(action)) {
            addProductToPromotion(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/sellercenter/promotion");
        }
    }

    private void addPromotion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            boolean type = Boolean.parseBoolean(request.getParameter("type"));
            int value = Integer.parseInt(request.getParameter("value"));
            String expireDate = request.getParameter("expireDate");
            int creatorId = Integer.parseInt(request.getParameter("creatorId"));

            Promotion promotion = new Promotion();
            promotion.setName(name);
            promotion.setType(type);
            promotion.setOfAdmin(false);
            promotion.setValue(value);
            promotion.setExpireDate(java.sql.Date.valueOf(java.time.LocalDate.parse(expireDate, config.Config.Time.inputFormatDate)));
            promotion.setCreationDate(java.sql.Date.valueOf(java.time.LocalDate.now()));

            User creator = dao.UserDAO.UserFetcher.getUser(creatorId);
            promotion.setCreatorId(creator);
            promotion.setStatus(true);

            Promotion dbPromotion = dao.PromotionDAO.PromotionManager.addPromotion(promotion);

            if (dbPromotion != null) {
                response.sendRedirect(request.getContextPath() + "/sellercenter/promotion");
            } else {
                request.setAttribute("error", "Failed to add promotion.");
                request.getRequestDispatcher(config.Config.JSPMapper.SHOP_ADD_PROMOTION).forward(request, response);
            }
        } catch (Exception e) {
            service.Logging.logger.error("Error adding promotion: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

    private void addProductToPromotion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String promotionIdStr = request.getParameter("promotionId");
        String[] productIds = request.getParameterValues("productIds");

        if (promotionIdStr == null || productIds == null || productIds.length == 0) {
            request.setAttribute("error", "Haven't opted for product");
            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_PROMOTION_DETAILS).forward(request, response);
            return;
        }

        try {
            int promotionId = Integer.parseInt(promotionIdStr);
            Promotion selectedPromotion = dao.PromotionDAO.PromotionFetcher.getPromotion(promotionId);

            if (selectedPromotion == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid promotion ID.");
                return;
            }

            for (String productIdStr : productIds) {
                int productId = Integer.parseInt(productIdStr);
                Product product = dao.ProductDAO.ProductFetcher.getProduct(productId);

                if (product != null) {
                    product.setAvailablePromotionId(selectedPromotion);
                    dao.ProductDAO.ProductManager.editProduct(product);
                }
            }

            response.sendRedirect(request.getContextPath() + "/promotion?action=products&promotionId=" + promotionId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format.");
        } catch (Exception e) {
            service.Logging.logger.error("Error adding products to promotion: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }
}
