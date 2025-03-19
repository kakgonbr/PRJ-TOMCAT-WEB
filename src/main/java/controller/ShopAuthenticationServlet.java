package controller;

import dao.ShopDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import model.Shop;
import model.User;

public class ShopAuthenticationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!sessionChecker(request, response)) {
            return;
        }
        HttpSession session = request.getSession();
        int shopId = getShop(request, response);
        service.Logging.logger.info("ShopAuthenticationServlet - Found shopId: " + shopId);
        if (shopId != -1) {
            session.setAttribute("shopId", shopId);
            response.sendRedirect(request.getContextPath() + "/shophome");
        } else {
            response.sendRedirect(request.getContextPath() + "/shop-signup");
        }
    }

    private boolean sessionChecker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private int getShop(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return -1;
        }

        try {
            Shop shop = ShopDAO.ShopFetcher.getShopByOwnerId(user.getId());
            return (shop != null) ? shop.getId() : -1;
        } catch (SQLException ex) {
            service.Logging.logger.error("ShopAuthenticationServlet - SQL Exception while fetching shop: ", ex);
        }

        return -1; 
    }
}
