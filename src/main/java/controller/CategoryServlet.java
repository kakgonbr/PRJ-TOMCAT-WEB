package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // model.Category category = null;
        // int categoryId = request.getParameter("categoryId") == null ? 0 : Integer.parseInt(request.getParameter("categoryId"));

        // try {
        //     category = dao.CategoryDAO.CategoryFetcher.getCategoryDetails(categoryId);

        //     request.setAttribute("category", new model.CategoryWrapper(category));

        // } catch (java.sql.SQLException e) {
        //     request.setAttribute("code", 404);
        //     request.getRequestDispatcher("/error").forward(request, response);

        //     return;
        // }
        // request.getRequestDispatcher(config.Config.JSPMapper.CATEGORY_DETAILS).forward(request, response);
        request.getRequestDispatcher("/search").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
