package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class UserOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.User user = (model.User) request.getSession(false).getAttribute("user");
        String action= request.getParameter("action");
        switch (action) {
            case "order":
                request.setAttribute("OrderItemList", service.UserOrderService.getOrderItems(user.getId(), 1));
                request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER).forward(request, response);
                break;
            case "complete":
                request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER_COMPLETE).forward(request, response);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    
}
