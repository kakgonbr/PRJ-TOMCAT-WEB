package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ShopServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");
        model.Shop shop = null;
        String shopIdStr = request.getParameter("shopId");  // Lấy từ request
        if (shopIdStr == null) {
            Object sessionShopId = session.getAttribute("shopId");
            if (sessionShopId instanceof Integer) {
                shopIdStr = String.valueOf(sessionShopId); // Chuyển Integer về String nếu cần
            } else if (sessionShopId instanceof String) {
                shopIdStr = (String) sessionShopId;
            }
        }
        int shopId = (shopIdStr == null) ? -1 : Integer.parseInt(shopIdStr);
        try {
            if (shopId != -1) {
                shop = dao.ShopDAO.ShopFetcher.getShop(shopId);
                request.setAttribute("shop", new model.ShopWrapper(shop));
            }

        } catch (java.sql.SQLException e) {
            request.setAttribute("code", 404);
            request.getRequestDispatcher("/error").forward(request, response);

            return;
        }

        // special page for shop owner here
        if (user != null && shop != null && shop.getOwnerId() != null && shop.getOwnerId().getId() == user.getId()) {
            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_OWNER_DETAILS).forward(request, response);

            return;
        }

        request.getRequestDispatcher(config.Config.JSPMapper.SHOP_DETAILS).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
