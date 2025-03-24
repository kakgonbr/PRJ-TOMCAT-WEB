package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");

        if (productId != null) {

            request.getRequestDispatcher(config.Config.JSPMapper.PRODUCT_INFO).forward(request, response);

            return;
        }


        request.setAttribute("code", 404);
        request.getRequestDispatcher("/error").forward(request, response);

        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            handleDeleteProduct(request, response);
        } else if ("restore".equals(action)) {
            handleRestoreProduct(request, response);
        }
    }

    
    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String productIdParam = request.getParameter("productId");
        try {
            int productId = Integer.parseInt(productIdParam);
            dao.ProductDAO.ProductManager.deleteProduct(productId);
            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "number format occur");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        } catch (java.sql.SQLException e) {
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        }
    }
    //handle restore product
    private void handleRestoreProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String productIdParam = request.getParameter("productId");
        try {
            int productId = Integer.parseInt(productIdParam);

            Product product = dao.ProductDAO.ProductFetcher.getProductDetails(productId);
            if (product != null) {
                product.setStatus(true);
                dao.ProductDAO.ProductManager.editProduct(product);
            }

            response.sendRedirect(request.getContextPath() + "/sellercenter/shophome");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID format.");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        } catch (java.sql.SQLException e) {
            request.setAttribute("error", "Database error occurred.");
            request.getRequestDispatcher(config.Config.JSPMapper.SELLER_CENTER).forward(request, response);
        }
    }
}

