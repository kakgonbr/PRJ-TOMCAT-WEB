package controller.ajax;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShopLoader extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // for now, no parameter

        try {
            java.util.List<model.ShopWrapper> shops = dao.ShopDAO.ShopFetcher.getShops().stream().map(model.ShopWrapper::new).toList();

            response.setContentType("application/json");

            String json = new com.google.gson.Gson().toJson(shops);

            service.Logging.logger.info("Sending back json {}", json);

            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET SHOPS, REASON: {}", e.getMessage());

            return;
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
