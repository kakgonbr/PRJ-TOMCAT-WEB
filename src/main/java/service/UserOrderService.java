package service;

import java.util.List;

import model.OrderItemWrapper;
import model.OrderedItem;
import model.ProductOrder;

public class UserOrderService {

    public static List<OrderItemWrapper> getOrderItems(int userId, int status) {
        try {

            List<ProductOrder> orders = dao.OrderDAO.OrderManager.getOrderFromUser(userId, status);
            List<OrderItemWrapper> orderItems = null;
            for(ProductOrder order : orders)
            {
                for(OrderedItem orderItem : order.getOrderedItemList()) {
                    orderItems.add(new OrderItemWrapper(orderItem));
                }
            }
            return orderItems;
            
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("FAILED TO GET AVAILABLE PROMOTIONS FOR USER {}");
        }
        return null;
    }
}
