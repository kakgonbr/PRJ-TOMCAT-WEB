package model;

import java.math.BigDecimal;
import java.util.Date;
public class OrderItemWrapper implements java.io.Serializable {
    private Integer id, userId, quantity, orderId;
    private BigDecimal totalPrice;
    private boolean status;
    private ProductWrapper productWrapper;
    private ProductItemWrapper productItem;
    private Date date;

    public OrderItemWrapper() {
    }
    
    public OrderItemWrapper(model.OrderedItem orderItem) {
        setId(orderItem.getId());
        setUserId(orderItem.getOrderId().getUserId().getId());
        setTotalPrice(orderItem.getTotalPrice());
        setStatus(orderItem.getOrderId().isStatus());
        setProductItem(new ProductItemWrapper(orderItem.getProductItemId()));
        setProductWrapper(new ProductWrapper(orderItem.getProductItemId().getProductId()));
        setQuantity(orderItem.getQuantity());
        setOrderId(orderItem.getOrderId().getId());
        setDate(orderItem.getOrderId().getDate());
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ProductWrapper getProductWrapper() {
        return productWrapper;
    }

    public void setProductWrapper(ProductWrapper productWrapper) {
        this.productWrapper = productWrapper;
    }

    public ProductItemWrapper getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItemWrapper productItem) {
        this.productItem = productItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    
}
