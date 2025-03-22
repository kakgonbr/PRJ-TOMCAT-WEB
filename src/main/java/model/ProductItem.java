/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblProductItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductItem.findAll", query = "SELECT p FROM ProductItem p"),
    @NamedQuery(name = "ProductItem.findById", query = "SELECT p FROM ProductItem p WHERE p.id = :id"),
    @NamedQuery(name = "ProductItem.findByStock", query = "SELECT p FROM ProductItem p WHERE p.stock = :stock"),
    @NamedQuery(name = "ProductItem.findByPrice", query = "SELECT p FROM ProductItem p WHERE p.price = :price")})
public class ProductItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "stock")
    private Integer stock;
     // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product productId;
    
    @OneToMany(mappedBy = "productItemId")
    private List<CartItem> cartItemList;
    
    @OneToMany(mappedBy = "productItemId")
    private List<OrderedItem> orderedItemList;
    
    @OneToMany(mappedBy = "productItemId")
    private List<ProductCustomization> productCustomizationList;
    // private String name;

    public ProductItem() {
    }

    public ProductItem(Integer id) {
        this.id = id;
    }
//     public String getName() {
//     return this.name; // Hoặc thuộc tính phù hợp
// }

    // Getters and Setters
     public Integer getId() {
         return id;
     }
 
     public void setId(Integer id) {
         this.id = id;
     }
 
     public Integer getStock() {
         return stock;
     }
 
     public void setStock(Integer stock) {
         this.stock = stock;
     }
 
     public BigDecimal getPrice() {
         return price;
     }
 
     public void setPrice(BigDecimal price) {
         this.price = price;
     }
 
     public Product getProductId() {
         return productId;
     }
 
     public void setProductId(Product productId) {
         this.productId = productId;
     }

    @XmlTransient
    public List<CartItem> getCartItemList() { return cartItemList; }
    public void setCartItemList(List<CartItem> cartItemList) { this.cartItemList = cartItemList; }

    @XmlTransient
    public List<OrderedItem> getOrderedItemList() { return orderedItemList; }
    public void setOrderedItemList(List<OrderedItem> orderedItemList) { this.orderedItemList = orderedItemList; }

    @XmlTransient
    public List<ProductCustomization> getProductCustomizationList() { return productCustomizationList; }
    public void setProductCustomizationList(List<ProductCustomization> productCustomizationList) { this.productCustomizationList = productCustomizationList; }

    @Override
    public int hashCode() {
         int hash = 0;
         hash += (id != null ? id.hashCode() : 0);
         return hash;
     }

    @Override
    public boolean equals(Object object) {
        ProductItem other = (ProductItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
             return false;
         }
         return true;
    }

    @Override
    public String toString() {
        return "model.ProductItem[ id=" + id + " ]";
    }
}