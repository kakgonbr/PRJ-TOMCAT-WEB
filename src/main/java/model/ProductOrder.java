/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblOrder")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "ProductOrder.findAll", query = "SELECT p FROM ProductOrder p"),
        @NamedQuery(name = "ProductOrder.findById", query = "SELECT p FROM ProductOrder p WHERE p.id = :id"),
        @NamedQuery(name = "ProductOrder.findByShippingAddress", query = "SELECT p FROM ProductOrder p WHERE p.shippingAddress = :shippingAddress"),
        @NamedQuery(name = "ProductOrder.findByDate", query = "SELECT p FROM ProductOrder p WHERE p.date = :date"),
        @NamedQuery(name = "ProductOrder.findByFinalPrice", query = "SELECT p FROM ProductOrder p WHERE p.finalPrice = :finalPrice") })
public class ProductOrder implements Serializable {

    @Size(max = 100)
    @Column(name = "shippingAddress")
    private String shippingAddress;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "finalPrice")
    private BigDecimal finalPrice;
    @JoinColumn(name = "paymentMethodId", referencedColumnName = "id")
    @ManyToOne
    private PaymentMethod paymentMethodId;
    @JoinColumn(name = "promotionId", referencedColumnName = "id")
    @ManyToOne
    private Promotion promotionId;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    @Column(name = "status")
    private Boolean status;
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.REMOVE)
    private List<OrderedItem> orderedItemList;

    public ProductOrder() {
    }

    public ProductOrder(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public PaymentMethod getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(PaymentMethod paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Promotion getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Promotion promotionId) {
        this.promotionId = promotionId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean isStatus() {
        return status;
    }

    @XmlTransient
    public List<OrderedItem> getOrderedItemList() {
        return orderedItemList;
    }

    public void setOrderedItemList(List<OrderedItem> orderedItemList) {
        this.orderedItemList = orderedItemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ProductOrder)) {
            return false;
        }
        ProductOrder other = (ProductOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ProductOrder[ id=" + id + " ]";
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}

