package controller.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MapAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");   
        switch (action) {
            case "auto":
                String address = service.MapAPIService.getAddressAuto(request.getParameter("query"));
                out.print(address);
                out.flush();    
                return;
            case "shippingFee":
                try {
                    String origin = request.getParameter("addressOrigin");
                    String destination = request.getParameter("addressDestination");
                    
                    service.Logging.logger.info("Calculating shipping fee from {} to {}", origin, destination);
                    
                    if (origin == null || origin.trim().isEmpty() || 
                        destination == null || destination.trim().isEmpty()) {
                        String errorResponse = "{\"status\":\"ERROR\",\"message\":\"Missing address parameters\"}";
                        service.Logging.logger.error("Invalid request: {}", errorResponse);
                        out.print(errorResponse);
                        return;
                    }
            
                    String distance = service.MapAPIService.getDistance(
                        service.MapAPIService.getLongLat(origin), 
                        service.MapAPIService.getLongLat(destination)
                    );
                    
                    service.Logging.logger.info("Distance calculated: {}", distance);
                    
                    Double shippingFee = service.MapAPIService.getShippingFee(distance);
                    
                    if (shippingFee == null) {
                        String errorResponse = "{\"status\":\"ERROR\",\"message\":\"Could not calculate shipping fee\"}";
                        service.Logging.logger.error("Shipping fee calculation failed: {}", errorResponse);
                        out.print(errorResponse);
                        return;
                    }
                    
                    String jsonResponse = String.format(
                        "{\"status\":\"OK\",\"fee\":%.1f}", 
                        shippingFee
                    );
                    service.Logging.logger.info("Shipping fee calculated successfully: {}", jsonResponse);
                    out.print(jsonResponse);
                } 
                catch (Exception e) {
                    service.Logging.logger.error("Error calculating shipping fee", e);
                    String errorResponse = String.format(
                        "{\"status\":\"ERROR\",\"message\":\"%s\"}", 
                        e.getMessage().replace("\"", "'")
                    );
                    out.print(errorResponse);
                } 
                finally {
                    out.flush();
                }
                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
}
