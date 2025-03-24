/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Promotion;
import model.User;

public class PromotionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getId();

        try {
            List<Promotion> promotions = dao.PromotionDAO.PromotionFetcher.checkAvailablePromotions(userId);

            request.setAttribute("promotions", promotions);
            request.getRequestDispatcher("displayPromotion.jsp").forward(request, response);
        } catch (Exception e) {
            service.Logging.logger.error("Error fetching promotions: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("promotionName");
        boolean type = request.getParameter("type").equals("1");
        int value = Integer.parseInt(request.getParameter("value"));
        String expireDate = request.getParameter("expireDate");
        int creatorId = Integer.parseInt(request.getParameter("creatorId"));

        // Tạo đối tượng promotion
        model.Promotion promotion = new model.Promotion();
        promotion.setName(name);
        promotion.setType(type);
        promotion.setValue(value);
        promotion.setExpireDate(java.sql.Date.valueOf(expireDate));
        User creator = null;
        try {
            creator = dao.UserDAO.UserFetcher.getUser(creatorId);
        } catch (SQLException ex) {
            Logger.getLogger(PromotionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        promotion.setCreatorId(creator);
        promotion.setStatus(true); // Mặc định là hoạt động

        try {
            // Thêm promotion vào DB
            boolean success = dao.PromotionDAO.PromotionManager.addPromotion(creatorId, promotion);

            if (success) {
                response.sendRedirect("promotion"); // Quay về trang danh sách promotions
            } else {
                request.setAttribute("error", "Failed to add promotion.");
                request.getRequestDispatcher("addPromotion.jsp").forward(request, response);
            }
        } catch (Exception e) {
            service.Logging.logger.error("Error adding promotion: {}", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }
}
