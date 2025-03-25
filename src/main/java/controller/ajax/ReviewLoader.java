package controller.ajax;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int pageSize = 5;
        try {
            List<Review> reviews = ReviewDAO.ReviewFetcher.getReviewsByProductId(productId, page, pageSize);
            long totalReviews = ReviewDAO.ReviewFetcher.getTotalReviews(productId);
            int totalPages = (int) Math.ceil((double) totalReviews / pageSize);
            
            Map<String, Object> reviewWrappers = new HashMap<>();
            reviewWrappers.put("reviews", reviews.stream().map(ReviewWrapper::new).toList());
            reviewWrappers.put("currentPage", page);
            reviewWrappers.put("totalPages", totalPages);
            reviewWrappers.put("totalReviews", totalReviews);
            
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
