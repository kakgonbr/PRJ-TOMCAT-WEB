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
                    
                    if (origin == null || origin.trim().isEmpty() || 
                        destination == null || destination.trim().isEmpty()) {
                        out.print("{\"status\":\"ERROR\",\"message\":\"Missing address parameters\"}");
                        return;
                    }
            
                    String distance = service.MapAPIService.getDistance(
                        service.MapAPIService.getLongLat(origin), 
                        service.MapAPIService.getLongLat(destination)
                    );
                    
                    Double shippingFee = service.MapAPIService.getShippingFee(distance);
                    
                    if (shippingFee == null) {
                        out.print("{\"status\":\"ERROR\",\"message\":\"Could not calculate shipping fee\"}");
                        return;
                    }
                    
                    String jsonResponse = String.format(
                        "{\"status\":\"OK\",\"fee\":%.1f}", 
                        shippingFee
                    );
                    out.print(jsonResponse);
                } catch (Exception e) {
                    service.Logging.logger.error("Error calculating shipping fee", e);
                    out.print("{\"status\":\"ERROR\",\"message\":\"" + e.getMessage() + "\"}");
                } finally {
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
