/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblOrderedItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderedItem.findAll", query = "SELECT o FROM OrderedItem o"),
    @NamedQuery(name = "OrderedItem.findById", query = "SELECT o FROM OrderedItem o WHERE o.id = :id"),
    @NamedQuery(name = "OrderedItem.findByOrderStatus", query = "SELECT o FROM OrderedItem o WHERE o.orderStatus = :orderStatus"),
    @NamedQuery(name = "OrderedItem.findByQuantity", query = "SELECT o FROM OrderedItem o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "OrderedItem.findByTotalPrice", query = "SELECT o FROM OrderedItem o WHERE o.totalPrice = :totalPrice"),
    @NamedQuery(name = "OrderedItem.findByShippingCost", query = "SELECT o FROM OrderedItem o WHERE o.shippingCost = :shippingCost")})
public class OrderedItem implements Serializable {

    @Size(max = 30)
    @Column(name = "orderStatus")
    private String orderStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalPrice")
    private BigDecimal totalPrice;
    @Column(name = "shippingCost")
    private BigDecimal shippingCost;
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    @ManyToOne
    private ProductOrder orderId;
    @JoinColumn(name = "productItemId", referencedColumnName = "id")
    @ManyToOne
    private ProductItem productItemId;

    public OrderedItem() {
    }

    public OrderedItem(Integer id) {
        this.id = id;
    }

    public OrderedItem(Integer id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ProductOrder getOrderId() {
        return orderId;
    }

    public void setOrderId(ProductOrder orderId) {
        this.orderId = orderId;
    }

    public ProductItem getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(ProductItem productItemId) {
        this.productItemId = productItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof OrderedItem)) {
            return false;
        }
        OrderedItem other = (OrderedItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.OrderedItem[ id=" + id + " ]";
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
