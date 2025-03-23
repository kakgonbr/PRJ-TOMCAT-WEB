/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dto;

import java.util.Date;

/**
 *
 * @author hoahtm
 */
public class OrderedItemDTO {
        private String userName;
        private String productName;
        private Double totalPrice;
        private Date date;
        private Double shippingCost;

        public OrderedItemDTO() {
        }

        public OrderedItemDTO(String userName, String productName, Double totalPrice, Date date, Double shippingCost) {
            this.userName = userName;
            this.productName = productName;
            this.totalPrice = totalPrice;
            this.date = date;
            this.shippingCost = shippingCost;
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

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Double getShippingCost() {
            return shippingCost;
        }

        public void setShippingCost(Double shippingCost) {
            this.shippingCost = shippingCost;
        }
    }
