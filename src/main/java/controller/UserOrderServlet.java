package controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

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
        List<OrderItemWrapper> orderItemWrappers = null;
        switch (action) {
            case "order":
                orderItemWrappers= service.UserOrderService.getOrderItems(user.getId(), false);
                if(orderItemWrappers != null) {
                    request.setAttribute("OrderItemList", orderItemWrappers);
                    request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER).forward(request, response);
                }
                break;
            case "complete":
                orderItemWrappers= service.UserOrderService.getOrderItems(user.getId(), true);
                if(orderItemWrappers != null) {
                    request.setAttribute("OrderItemList", orderItemWrappers);
                    request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER_COMPLETE).forward(request, response);
                }
                break;
            default:
                break;
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.User user = (model.User) request.getSession(false).getAttribute("user");
        String action= request.getParameter("action");
        String status = request.getParameter("status");
        List<OrderItemWrapper> orderItemWrappers = null;
        
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        Date startDate = null;
        Date endDate = null;
        
        if (startDateStr != null && !startDateStr.isEmpty()) {
            try {
                startDate = java.sql.Date.valueOf(startDateStr);
            } catch (IllegalArgumentException e) {
                service.Logging.logger.error("Invalid start date format: " + startDateStr);
            }
        }
        
        if (endDateStr != null && !endDateStr.isEmpty()) {
            try {
                // Add one day to include the end date in search
                endDate = java.sql.Date.valueOf(endDateStr);
                Calendar c = Calendar.getInstance();
                c.setTime(endDate);
                c.add(Calendar.DATE, 1);
                endDate = new java.sql.Date(c.getTimeInMillis());
            } catch (IllegalArgumentException e) {
                service.Logging.logger.error("Invalid end date format: " + endDateStr);
            }
        }
        orderItemWrappers= service.UserOrderService.getOrderItems(user.getId(), Boolean.parseBoolean(status), startDate, endDate);
        if(orderItemWrappers != null) {
            request.setAttribute("OrderItemList", orderItemWrappers);
        }
        else
            request.setAttribute("message", "Không tìm thấy đơn hàng theo ngày đã nhập");
        if(action.equalsIgnoreCase("order"))
            request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER).forward(request, response);
        else
            request.getRequestDispatcher(config.Config.JSPMapper.USER_ORDER_COMPLETE).forward(request, response);
        return;
    }

    
}
