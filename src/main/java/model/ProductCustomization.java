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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblProductCustomization")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductCustomization.findAll", query = "SELECT p FROM ProductCustomization p"),
    @NamedQuery(name = "ProductCustomization.findById", query = "SELECT p FROM ProductCustomization p WHERE p.id = :id")})
public class ProductCustomization implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "productItemId", referencedColumnName = "id")
    @ManyToOne
    private ProductItem productItemId;
    @JoinColumn(name = "variationValueId", referencedColumnName = "id")
    @ManyToOne
    private VariationValue variationValueId;

    public ProductCustomization() {
    }

    public ProductCustomization(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductItem getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(ProductItem productItemId) {
        this.productItemId = productItemId;
    }

    public VariationValue getVariationValueId() {
        return variationValueId;
    }

    public void setVariationValueId(VariationValue variationValueId) {
        this.variationValueId = variationValueId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductCustomization)) {
            return false;
        }
        ProductCustomization other = (ProductCustomization) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ProductCustomization[ id=" + id + " ]";
    }
    
}
