package service;

import java.util.List;

import model.OrderItemWrapper;


public class UserOrderService {

    public static List<OrderItemWrapper> getOrderItems(int userId) {
        try {
            List<model.OrderedItem> orderedItems = dao.OrderDAO.OrderedItemManager.getOrderItemFromUser(userId);
            if (!orderedItems.isEmpty()) {
                List<OrderItemWrapper> orderItemWrappers = orderedItems.stream().map(OrderItemWrapper::new).toList();
                service.Logging.logger.info("Successfully retrieved order items for user: " + userId);
                service.Logging.logger.info("List of order items:" + orderItemWrappers.toString());
                return orderItemWrappers;
            } else {
                service.Logging.logger.warn("No orders found for user: " + userId);
                return null;
            }
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("Failed to get order items for user: " + userId);
            return null;
        }
    }

}
