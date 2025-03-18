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
        int shopId = Integer.parseInt(request.getParameter("shopId"));

        try {
            shop = dao.ShopDAO.ShopFetcher.getShop(shopId);

            request.setAttribute("shop", new model.ShopWrapper(shop));

        } catch (java.sql.SQLException e) {
            request.setAttribute("code", 404);
            request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);

            return;
        }

        if (user == null || shop.getOwnerId().getId() != user.getId()) {
            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_DETAILS).forward(request, response);

            return;
        }

        // special page for shop owner here
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
