package controller.ajax;

import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CategoryLoader extends HttpServlet {
    /**
     * For now, this is only used for retrieving a tree-like hierarchy of categories for the filter
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer categoryId = request.getParameter("categoryId") == null ? null : Integer.parseInt(request.getParameter("categoryId"));

        try {
            // Really inefficient
            // java.util.List<model.CategoryWrapper> categories = dao.CategoryDAO.CategoryFetcher.getAllCategories().stream().map(model.CategoryWrapper::new).collect(Collectors.toList()); 

            model.CategoryWrapper wrapper = new model.CategoryWrapper(dao.CategoryDAO.CategoryFetcher.getTopCategory(categoryId == null ? 0 : categoryId));


            response.setContentType("application/json");

            String json = new com.google.gson.Gson().toJson(wrapper);

            response.getWriter().write(json);
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET FILTERS, REASON: {}", e.getMessage());

            return;
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
