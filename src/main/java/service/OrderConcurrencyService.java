package service;

public class OrderConcurrencyService {
    /**
     * Called when the payment the order is tied to is invalid.
     * @param productItemId
     * @param quantity
     * @throws java.sql.SQLException
     */
    public static void removeFromOrder(int orderId) throws java.sql.SQLException {
        model.ProductOrder order = dao.OrderDAO.OrderManager.getOrder(orderId, true);

        for (final model.OrderedItem orderedItem : order.getOrderedItemList()) {
            model.ProductItem productItem = orderedItem.getProductItemId();
            productItem.setStock(productItem.getStock() + orderedItem.getQuantity()); // entity manager is not active here

            dao.ProductDAO.ProductManager.updateProductItem(productItem);
        }
    }
}
