package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderItemWrapper;


public class UserOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.User user = (model.User) request.getSession(false).getAttribute("user");
        String action= request.getParameter("action");
        switch (action) {
            case "order":
                List<OrderItemWrapper> orderItemWrappers= service.UserOrderService.getOrderItems(user.getId());
                if(orderItemWrappers != null) {
                    request.setAttribute("OrderItemList", orderItemWrappers);
                    request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER).forward(request, response);
                }
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
