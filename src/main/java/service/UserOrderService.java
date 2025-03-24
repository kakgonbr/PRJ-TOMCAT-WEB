package service;

import java.util.List;

import model.OrderItemWrapper;


public class UserOrderService {

    public static List<OrderItemWrapper> getOrderItems(int userId) {
        try {
            List<OrderItemWrapper> orderItems = dao.OrderDAO.OrderedItemManager.getOrderItemFromUser(userId).stream().map(model.OrderItemWrapper::new).toList();
            if(!orderItems.isEmpty())
                service.Logging.logger.info("List of orderItems: " + orderItems.toString());
            else
                service.Logging.logger.warn("orderitem dao error or there's no order");
            return orderItems;
            
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET ORDERITEMS");
        }
        return null;
    }
}
