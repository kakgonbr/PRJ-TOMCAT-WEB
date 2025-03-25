package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
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
            String promotionIdStr = request.getParameter("promotionId");
            Integer shopId = (Integer) session.getAttribute("shopId");

            if (promotionIdStr == null || shopId == null) {
                response.sendRedirect(request.getContextPath() + "/promotion");
                return;
            }

            try {
                int promotionId = Integer.parseInt(promotionIdStr);

                // List<Product> products = dao.ProductDAO.ProductFetcher.getShopProductsByPromotion(shopId, promotionId); // getShopProductsByPromotion is not defined
                // request.setAttribute("products", products);
                request.getRequestDispatcher(config.Config.JSPMapper.SHOP_PROMOTION_DETAILS).forward(request, response);
            } catch (NumberFormatException e) {
                service.Logging.logger.error("Invalid promotionId or shopId format: {}", e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid promotionId or shopId format.");
            } catch (Exception e) {
                service.Logging.logger.error("Error fetching products: {}", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
            }
            return;
        }

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
        String name = request.getParameter("promotionName");
        boolean type = Boolean.parseBoolean(request.getParameter("type"));
        int value = Integer.parseInt(request.getParameter("value"));
        String expireDate = request.getParameter("expireDate");
        int creatorId = Integer.parseInt(request.getParameter("creatorId"));

        model.Promotion promotion = new model.Promotion();
        promotion.setName(name);
        promotion.setType(type);
        promotion.setOfAdmin(false);
        promotion.setValue(value);
        promotion.setExpireDate(java.sql.Date.valueOf(expireDate));
        User creator = null;
        try {
            creator = dao.UserDAO.UserFetcher.getUser(creatorId);
        } catch (SQLException e) {
            service.Logging.logger.error("Error: {}", e);
        }
        promotion.setCreatorId(creator);
        promotion.setStatus(true);

        try {
            // Thêm promotion vào DB
            model.Promotion dbPromotion = dao.PromotionDAO.PromotionManager.addPromotion(promotion);

            if (dbPromotion != null) {
                response.sendRedirect(request.getContextPath() + "/sellercenter/promotion"); // Quay về trang danh sách promotions
            } else {
                request.setAttribute("error", "Failed to add promotion.");
                request.getRequestDispatcher(config.Config.JSPMapper.SHOP_ADD_PROMOTION).forward(request, response);
            }
        } catch (Exception e) {
            service.Logging.logger.error("Error adding promotion: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }
}
