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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblServerStatistics")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblServerStatistics.findAll", query = "SELECT t FROM TblServerStatistics t"),
    @NamedQuery(name = "TblServerStatistics.findById", query = "SELECT t FROM TblServerStatistics t WHERE t.id = :id"),
    @NamedQuery(name = "TblServerStatistics.findByDay", query = "SELECT t FROM TblServerStatistics t WHERE t.day = :day"),
    @NamedQuery(name = "TblServerStatistics.findByTotalMoneyEarned", query = "SELECT t FROM TblServerStatistics t WHERE t.totalMoneyEarned = :totalMoneyEarned"),
    @NamedQuery(name = "TblServerStatistics.findByUserNum", query = "SELECT t FROM TblServerStatistics t WHERE t.userNum = :userNum"),
    @NamedQuery(name = "TblServerStatistics.findByProductNum", query = "SELECT t FROM TblServerStatistics t WHERE t.productNum = :productNum"),
    @NamedQuery(name = "TblServerStatistics.findByShopNum", query = "SELECT t FROM TblServerStatistics t WHERE t.shopNum = :shopNum"),
    @NamedQuery(name = "TblServerStatistics.findByPromotionNum", query = "SELECT t FROM TblServerStatistics t WHERE t.promotionNum = :promotionNum"),
    @NamedQuery(name = "TblServerStatistics.findByPurchaseNum", query = "SELECT t FROM TblServerStatistics t WHERE t.purchaseNum = :purchaseNum"),
    @NamedQuery(name = "TblServerStatistics.findByVisitNum", query = "SELECT t FROM TblServerStatistics t WHERE t.visitNum = :visitNum"),
    @NamedQuery(name = "TblServerStatistics.findByPeakSessionNum", query = "SELECT t FROM TblServerStatistics t WHERE t.peakSessionNum = :peakSessionNum"),
    @NamedQuery(name = "TblServerStatistics.findByAverageResponseTime", query = "SELECT t FROM TblServerStatistics t WHERE t.averageResponseTime = :averageResponseTime"),
    @NamedQuery(name = "TblServerStatistics.findByMaxResponseTime", query = "SELECT t FROM TblServerStatistics t WHERE t.maxResponseTime = :maxResponseTime")})
public class TblServerStatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "day")
    @Temporal(TemporalType.DATE)
    private Date day;
    @Column(name = "totalMoneyEarned")
    private Integer totalMoneyEarned;
    @Column(name = "userNum")
    private Integer userNum;
    @Column(name = "productNum")
    private Integer productNum;
    @Column(name = "shopNum")
    private Integer shopNum;
    @Column(name = "promotionNum")
    private Integer promotionNum;
    @Column(name = "purchaseNum")
    private Integer purchaseNum;
    @Column(name = "visitNum")
    private Integer visitNum;
    @Column(name = "peakSessionNum")
    private Integer peakSessionNum;
    @Column(name = "averageResponseTime")
    private Integer averageResponseTime;
    @Column(name = "maxResponseTime")
    private Integer maxResponseTime;

    public TblServerStatistics() {
    }

    public TblServerStatistics(Integer id) {
        this.id = id;
    }

    public TblServerStatistics(Integer id, Date day) {
        this.id = id;
        this.day = day;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Integer getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    public void setTotalMoneyEarned(Integer totalMoneyEarned) {
        this.totalMoneyEarned = totalMoneyEarned;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public Integer getShopNum() {
        return shopNum;
    }

    public void setShopNum(Integer shopNum) {
        this.shopNum = shopNum;
    }

    public Integer getPromotionNum() {
        return promotionNum;
    }

    public void setPromotionNum(Integer promotionNum) {
        this.promotionNum = promotionNum;
    }

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public Integer getPeakSessionNum() {
        return peakSessionNum;
    }

    public void setPeakSessionNum(Integer peakSessionNum) {
        this.peakSessionNum = peakSessionNum;
    }

    public Integer getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(Integer averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
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
        if (!(object instanceof TblServerStatistics)) {
            return false;
        }
        TblServerStatistics other = (TblServerStatistics) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TblServerStatistics[ id=" + id + " ]";
    }
    
}
