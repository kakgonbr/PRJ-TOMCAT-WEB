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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=not_logged_in");
            return;
        }

        // Nhận dữ liệu từ form
        String shopName = request.getParameter("shopName");
        String address = request.getParameter("shopAddress");
        //validation!!
        if (shopName == null || !misc.Utils.Validator.username(shopName)) {
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

            dao.ShopDAO.ShopManager.createShop(shop);

            // Chuyển hướng đến trang home sau khi đăng ký thành công
            response.sendRedirect("home.jsp?success=shop_registered");

        } catch (SQLException e) {
            request.setAttribute("error", "database_error");
            request.getRequestDispatcher("registerShop.jsp").forward(request, response);
        }
    }
}
