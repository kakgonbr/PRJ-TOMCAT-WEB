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
import model.Product;
import model.Review;
import model.ReviewWrapper;
import model.User;

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
            Double averageRating = ReviewDAO.ReviewFetcher.getOverallRating(productId);
            
            Map<String, Object> reviewWrappers = new HashMap<>();
            reviewWrappers.put("reviews", reviews.stream().map(ReviewWrapper::new).toList());
            reviewWrappers.put("currentPage", page);
            reviewWrappers.put("totalPages", totalPages);
            reviewWrappers.put("totalReviews", totalReviews);
            reviewWrappers.put("averageRating", averageRating);

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(reviewWrappers);
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            service.Logging.logger.error("Error loading reviews for product ID: {}", productId, e);
        }
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rate = Integer.parseInt(request.getParameter("rate"));
            String comment = request.getParameter("comment");
            
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            Review review = new Review();
            review.setProductId(new Product(productId));
            review.setUserId(user);
            review.setRate(rate);
            review.setComment(comment);
            review.setStatus(true);
            
            boolean success = ReviewDAO.ReviewManager.addReview(review);
            
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", success);
            
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(jsonResponse));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            response.getWriter().write(new Gson().toJson(error));
        }
    }

}
