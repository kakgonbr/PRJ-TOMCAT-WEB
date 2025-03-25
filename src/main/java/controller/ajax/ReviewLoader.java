package controller.ajax;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import dao.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Review;
import model.ReviewWrapper;

public class ReviewLoader extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        int productId = Integer.parseInt(request.getParameter("productId"));
        try {
            List<Review> reviews = ReviewDAO.ReviewFetcher.getReviewsByProductId(productId);
            List<ReviewWrapper> reviewWrappers = reviews.stream()
                .map(ReviewWrapper::new)
                .toList();
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(reviewWrappers);
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            service.Logging.logger.error("Error loading reviews for product ID: {}", productId, e);
        }

    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
