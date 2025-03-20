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
import model.Variation;

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
        request.getRequestDispatcher(config.Config.JSPMapper.ADD_PRODUCT).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        model.User user = session == null ? null : (model.User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("addProduct".equals(action)) {
            handleAddProduct(request, response, session);
        } else if ("selectVariation".equals(action)) {
            handleSelectVariation(request, response, session);
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("category"));
            String name = request.getParameter("productName");
            String description = request.getParameter("description");
            Integer shopId = (Integer) session.getAttribute("shopId");

            if (shopId == null || categoryId == -1 || name == null || description == null) {
                request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
                return;
            }

            Product product = new Product();
            product.setShopId(new Shop(shopId));
            product.setCategoryId(new Category(categoryId));
            product.setName(name);
            product.setDescription(description);
            product.setStatus(true);

            dao.ProductDAO.ProductManager.addProduct(product);

            session.setAttribute("categoryId", categoryId);

            response.sendRedirect(request.getContextPath() + "/addproduct?action=selectVariation");
        } catch (SQLException | NumberFormatException e) {
            request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }

    private void handleSelectVariation(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        try {
            int categoryId = (Integer) session.getAttribute("categoryId");
            String variationName = request.getParameter("variationName");
            String variationOptions = request.getParameter("variationOptions");
            String unit = request.getParameter("unit");
            String datatype = request.getParameter("datatype"); 

            if (categoryId == -1 || variationName == null || variationOptions == null || unit == null || datatype == null) {
                request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
                return;
            }

            Variation variation = new Variation();
            variation.setCategoryId(new Category(categoryId));
            variation.setName(variationName);
            variation.setDatatype(datatype); 
            variation.setUnit(unit);

            dao.VariationDAO.VariationManager.createVariation(variation);

            response.sendRedirect(request.getContextPath() + "/addproduct?action=selectVariation&success=true");
        } catch (SQLException e) {
            request.getRequestDispatcher(config.Config.JSPMapper.ERROR_JSP).forward(request, response);
        }
    }
}
