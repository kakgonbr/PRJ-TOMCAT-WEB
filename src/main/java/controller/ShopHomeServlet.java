package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.Shop;
import model.Category;

public class ShopHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/addproduct".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/jsp/addProduct.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Integer shopId = (Integer) session.getAttribute("shopId");

        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/shop-signup");
            return;
        }

        request.setAttribute("shopId", shopId);
        request.getRequestDispatcher("/WEB-INF/jsp/sellercenter.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("addProduct".equals(action)) {
            addProduct(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/error");
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");

        int shopId = getIntParameter(request, "shopId");
        int categoryId = getIntParameter(request, "category");
        String name = request.getParameter("productName");
        String description = request.getParameter("description");

        if (user == null || shopId == -1 || categoryId == -1 || name == null || description == null) {
            response.sendRedirect(request.getContextPath() + "/error?code=400");
            return;
        }

        try {
            Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopId);
            if (shop == null || shop.getOwnerId().getId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/error?code=403");
                return;
            }

            Product product = new Product();
            product.setShopId(shop);
            product.setCategoryId(new Category(categoryId));
            product.setName(name);
            product.setDescription(description);
            product.setStatus(true);

            dao.ProductDAO.ProductManager.addProduct(product);
            response.sendRedirect(request.getContextPath() + "/selectVariation?productId=" + product.getId());
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/error?code=500");
        }
    }

    private int getIntParameter(HttpServletRequest request, String paramName) {
        try {
            return Integer.parseInt(request.getParameter(paramName));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
