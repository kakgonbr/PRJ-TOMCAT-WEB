package controller.ajax;

import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class VariationLoader extends HttpServlet {
    /**
     * For now, this is only used for retrieving a tree-like hierarchy of categories for the filter
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer variationId = request.getParameter("variationId") == null ? null : Integer.parseInt(request.getParameter("variationId"));

        try {

            model.VariationWrapper wrapper = new model.VariationWrapper(dao.VariationDAO.VariationFetcher.getTopVariation(variationId == null ? 0 : variationId));


            response.setContentType("application/json");

            String json = new com.google.gson.Gson().toJson(wrapper);

            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET VARIATION, REASON: {}", e.getMessage());

            return;
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
