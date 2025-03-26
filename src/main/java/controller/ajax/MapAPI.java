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
                String origin = request.getParameter("addressOrigin");
                String destination = request.getParameter("addressDestination");
                String distance = service.MapAPIService.getDistance(
                    service.MapAPIService.getLongLat(origin), 
                    service.MapAPIService.getLongLat(destination)
                );
                Double shippingFee = service.MapAPIService.getShippingFee(distance);
                
                String jsonResponse = String.format(
                    "{\"status\":\"OK\",\"fee\":%.1f}", 
                    shippingFee
                );
                out.print(jsonResponse);
                out.flush();    
                return;       
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
}
