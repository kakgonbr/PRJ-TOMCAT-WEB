package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.Shop;
import model.Category;
import config.Config;

@WebServlet(name = "AddProductServlet", urlPatterns = {"/addproduct"})
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer shopId = (Integer) session.getAttribute("shopId");

        if (shopId == null) {
            response.sendRedirect(request.getContextPath() + "/shop-signup");
            return;
        }

        request.setAttribute("shopId", shopId);
        request.getRequestDispatcher(Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");

        int shopId = request.getParameter("shopId") == null ? -1 : Integer.parseInt(request.getParameter("shopId"));
        int categoryId = request.getParameter("category") == null ? -1 : Integer.parseInt(request.getParameter("category"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        if (user == null || shopId == -1 || categoryId == -1 || name == null || description == null) {
            request.setAttribute("code", 400);
            request.getRequestDispatcher(Config.JSPMapper.ERROR_JSP).forward(request, response);
            return;
        }

        try {
            Shop shop = dao.ShopDAO.ShopFetcher.getShop(shopId);
            if (shop == null || shop.getOwnerId().getId() != user.getId()) {
                request.setAttribute("code", 403);
                request.getRequestDispatcher(Config.JSPMapper.ERROR_JSP).forward(request, response);
                return;
            }

            Product product = new Product();
            product.setShopId(shop);
            product.setCategoryId(new Category(categoryId));
            product.setName(name);
            product.setDescription(description);
            product.setStatus(true);

            dao.ProductDAO.ProductManager.addProduct(product);
            response.sendRedirect(request.getContextPath() + "/addproduct-success?productId=" + product.getId());
        } catch (SQLException e) {
            request.setAttribute("code", 500);
            request.getRequestDispatcher(Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }
}
