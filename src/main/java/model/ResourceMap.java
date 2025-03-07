/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "tblResourceMap")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResourceMap.findAll", query = "SELECT r FROM ResourceMap r"),
    @NamedQuery(name = "ResourceMap.findById", query = "SELECT r FROM ResourceMap r WHERE r.id = :id"),
    @NamedQuery(name = "ResourceMap.findBySystemPath", query = "SELECT r FROM ResourceMap r WHERE r.systemPath = :systemPath")})
public class ResourceMap implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "systemPath")
    private String systemPath;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "id")
    private String id;
    @OneToMany(mappedBy = "profileStringResourceId")
    private List<User> userList;
    @OneToMany(mappedBy = "imageStringResourceId")
    private List<Category> categoryList;
    @OneToMany(mappedBy = "imageStringResourceId")
    private List<Product> productList;
    @OneToMany(mappedBy = "imageStringResourceId")
    private List<ProductImage> productImageList;
    @OneToMany(mappedBy = "profileStringResourceId")
    private List<Shop> shopList;

    public ResourceMap() {
    }

    public ResourceMap(String id) {
        this.id = id;
    }

    public ResourceMap(String id, String systemPath) {
        this.id = id;
        this.systemPath = systemPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @XmlTransient
    public List<ProductImage> getProductImageList() {
        return productImageList;
    }

    public void setProductImageList(List<ProductImage> productImageList) {
        this.productImageList = productImageList;
    }

    @XmlTransient
    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
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
        if (!(object instanceof ResourceMap)) {
            return false;
        }
        ResourceMap other = (ResourceMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ResourceMap[ id=" + id + " ]";
    }

    public String getSystemPath() {
        return systemPath;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }
    
}
