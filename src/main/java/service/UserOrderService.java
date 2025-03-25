package service;

import java.sql.Date;
import java.util.List;

import model.OrderItemWrapper;


public class UserOrderService {

    public static List<OrderItemWrapper> getOrderItems(int userId, boolean status) {
        return getOrderItems(userId, status, null, null);
    }

    public static List<OrderItemWrapper> getOrderItems(int userId, boolean status, Date startDate, Date endDate) {
        try {
            List<model.OrderedItem> orderedItems = dao.OrderDAO.OrderedItemManager.getOrderItemFromUser(userId, status, startDate, endDate);
            if (!orderedItems.isEmpty()) {
                List<OrderItemWrapper> orderItemWrappers = orderedItems.stream()
                    .map(OrderItemWrapper::new)
                    .toList();
                service.Logging.logger.info("Successfully retrieved order items for user: " + userId);
                return orderItemWrappers;
            } else {
                service.Logging.logger.warn("No orders found for user: " + userId);
                return null;
            }
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to get order items for user: " + userId, e);
            return null;
        }
    }
    
}
