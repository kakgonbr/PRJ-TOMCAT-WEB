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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "tblVariation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Variation.findAll", query = "SELECT v FROM Variation v"),
    @NamedQuery(name = "Variation.findById", query = "SELECT v FROM Variation v WHERE v.id = :id"),
    @NamedQuery(name = "Variation.findByName", query = "SELECT v FROM Variation v WHERE v.name = :name"),
    @NamedQuery(name = "Variation.findByDatatype", query = "SELECT v FROM Variation v WHERE v.datatype = :datatype"),
    @NamedQuery(name = "Variation.findByUnit", query = "SELECT v FROM Variation v WHERE v.unit = :unit")})
public class Variation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 30)
    @Column(name = "name")
    private String name;
    @Size(max = 10)
    @Column(name = "datatype")
    private String datatype;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    @ManyToOne
    private Category categoryId;
    @OneToMany(mappedBy = "variationId")
    private List<VariationValue> variationValueList;

    public Variation() {
    }

    public Variation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    @XmlTransient
    public List<VariationValue> getVariationValueList() {
        return variationValueList;
    }

    public void setVariationValueList(List<VariationValue> variationValueList) {
        this.variationValueList = variationValueList;
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
        if (!(object instanceof Variation)) {
            return false;
        }
        Variation other = (Variation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Variation[ id=" + id + " ]";
    }
    
}
