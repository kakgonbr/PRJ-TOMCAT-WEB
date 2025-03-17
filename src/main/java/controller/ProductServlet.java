package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");

        try {
            request.setAttribute("product", new model.ProductDetailsWrapper(dao.ProductDAO.ProductFetcher.getProductDetails(Integer.parseInt(productId))));
        } catch (java.sql.SQLException | NumberFormatException e) {
            service.Logging.logger.warn("FAILED TO GET PRODUCT INFORMATION FOR PRODUCT ID {}, REASON: {}", productId, e.getMessage());

            request.setAttribute("error", "true");
        }

        request.getRequestDispatcher(config.Config.JSPMapper.PRODUCT_DETAILS).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
