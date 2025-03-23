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
        private int quantity;
        public OrderedItemDTO() {}

    public OrderedItemDTO(Object[] row) {
        this.userName = row[0] != null ? row[0].toString() : null;
        this.productName = row[1] != null ? row[1].toString() : null;
        this.quantity = row[2] != null ? ((Number) row[2]).intValue() : 0;
        this.totalPrice = row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO;
        this.shippingCost = row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO;
    }

    public model.OrderedItem toOrderedItem() {
        model.OrderedItem orderedItem = new model.OrderedItem();
        orderedItem.setTotalPrice(totalPrice);
        orderedItem.setShippingCost(shippingCost);
        orderedItem.setQuantity(quantity);
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
    
        public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
