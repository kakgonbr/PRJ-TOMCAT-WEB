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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblVariationValue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariationValue.findAll", query = "SELECT v FROM VariationValue v"),
    @NamedQuery(name = "VariationValue.findById", query = "SELECT v FROM VariationValue v WHERE v.id = :id"),
    @NamedQuery(name = "VariationValue.findByValue", query = "SELECT v FROM VariationValue v WHERE v.value = :value")})
public class VariationValue implements Serializable {

    @Size(max = 15)
    @Column(name = "value")
    private String value;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "status")
    private Boolean status;
    @JoinColumn(name = "variationId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Variation variationId;
    @OneToMany(mappedBy = "variationValueId")
    private List<ProductCustomization> productCustomizationList;

    public VariationValue() {
    }

    public VariationValue(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Variation getVariationId() {
        return variationId;
    }

    public void setVariationId(Variation variationId) {
        this.variationId = variationId;
    }

    @XmlTransient
    public List<ProductCustomization> getProductCustomizationList() {
        return productCustomizationList;
    }

    public void setProductCustomizationList(List<ProductCustomization> productCustomizationList) {
        this.productCustomizationList = productCustomizationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof VariationValue)) {
            return false;
        }
        VariationValue other = (VariationValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.VariationValue[ id=" + id + " ]";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
