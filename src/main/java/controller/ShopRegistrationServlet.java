/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import model.Shop;

/**
 *
 * @author hoahtm
 */
public class ShopRegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        model.User user = null;
        if (session != null) {
            user = (model.User) session.getAttribute("user");
        }

        if (user == null) {
            service.Logging.logger.warn("Unauthorized attempt to register shop. IP: {}, UserAgent: {}",
                    request.getRemoteAddr(), request.getHeader("User-Agent"));

            response.sendRedirect(request.getContextPath() + "/login?error=access_denied");
            return;
        }
        request.getRequestDispatcher(config.Config.JSPMapper.SHOP_SIGNUP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                HttpSession session = request.getSession(false);
        model.User user = (session != null) ? (model.User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=access_denied");
            return;
        }
        //just need shopname and address
        String shopName = request.getParameter("shopName");
        String address = request.getParameter("address");
        //validation!!
        if (shopName == null) {
            request.setAttribute("error", "shopName");

            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_SIGNUP).forward(request, response);

            return;
        }
        if (address == null || address.trim().isEmpty()) {
            request.setAttribute("error", "address");

            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_SIGNUP).forward(request, response);

            return;
        }
        model.Shop shop;
        try {

            shop = new Shop();
            shop.setName(shopName);
            shop.setAddress(address);
            shop.setOwnerId(user);
            shop.setVisible(true);
            shop.setProfileStringResourceId(new model.ResourceMap("test_png"));

            dao.ShopDAO.ShopManager.createShop(shop);

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome"); // doesnt work?
            // request.getRequestDispatcher(config.Config.JSPMapper.SHOP_OWNER_DETAILS).forward(request, response);

        } catch (SQLException e) {
            service.Logging.logger.error("Database error while registering shop for user {}: {}",
                    user.getId(), e.getMessage());

            request.setAttribute("error", "db");
            request.getRequestDispatcher(config.Config.JSPMapper.SHOP_SIGNUP).forward(request, response);
        }
    }
}
