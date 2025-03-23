/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hoahtm
 */
public class OrderedItemDTO {
        private String userName;
        private String productName;
        private BigDecimal totalPrice;
        private BigDecimal shippingCost;
        public OrderedItemDTO() {}

    public OrderedItemDTO(model.OrderedItem orderedItem) {
        if (orderedItem != null) {
            model.ProductOrder order = orderedItem.getOrderId();
            model.Product product = (orderedItem.getProductItemId() != null) ? orderedItem.getProductItemId().getProductId() : null;
            model.User user = (order != null) ? order.getUserId() : null;

            this.userName = (user != null) ? user.getDisplayName() : null;
            this.productName = (product != null) ? product.getName() : null;
            this.totalPrice = (orderedItem.getTotalPrice() != null) ? orderedItem.getTotalPrice() : BigDecimal.ZERO;
            this.shippingCost = (orderedItem.getShippingCost() != null) ? orderedItem.getShippingCost() : BigDecimal.ZERO;
        }
    }

    public model.OrderedItem toOrderedItem() {
        model.OrderedItem orderedItem = new model.OrderedItem();
        orderedItem.setTotalPrice(totalPrice);
        orderedItem.setShippingCost(shippingCost);
        return orderedItem;
    }

    // Getter & Setter
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }
}
