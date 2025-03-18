package controller;

import dao.ShopDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Shop;
import model.User;

public class ShopAuthenticationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!sessionChecker(request, response)) {
            return;
        }

        int shopId = getShop(request, response);
        if (shopId != -1) {
            response.sendRedirect(request.getContextPath() + "/jsp/shopInfo.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/shop-signup");
        }
    }

    private boolean sessionChecker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("jsp/login.jsp");
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
            Logger.getLogger(ShopAuthenticationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1; 
    }
}
