/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblPromotionUsage.findAll", query = "SELECT t FROM TblPromotionUsage t"),
    @NamedQuery(name = "TblPromotionUsage.findByUserId", query = "SELECT t FROM TblPromotionUsage t WHERE t.tblPromotionUsagePK.userId = :userId"),
    @NamedQuery(name = "TblPromotionUsage.findByPromotionId", query = "SELECT t FROM TblPromotionUsage t WHERE t.tblPromotionUsagePK.promotionId = :promotionId")})
public class TblPromotionUsage implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblPromotionUsagePK tblPromotionUsagePK;

    public TblPromotionUsage() {
    }

    public TblPromotionUsage(TblPromotionUsagePK tblPromotionUsagePK) {
        this.tblPromotionUsagePK = tblPromotionUsagePK;
    }

    public TblPromotionUsage(int userId, int promotionId) {
        this.tblPromotionUsagePK = new TblPromotionUsagePK(userId, promotionId);
    }

    public TblPromotionUsagePK getTblPromotionUsagePK() {
        return tblPromotionUsagePK;
    }

    public void setTblPromotionUsagePK(TblPromotionUsagePK tblPromotionUsagePK) {
        this.tblPromotionUsagePK = tblPromotionUsagePK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblPromotionUsagePK != null ? tblPromotionUsagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblPromotionUsage)) {
            return false;
        }
        TblPromotionUsage other = (TblPromotionUsage) object;
        if ((this.tblPromotionUsagePK == null && other.tblPromotionUsagePK != null) || (this.tblPromotionUsagePK != null && !this.tblPromotionUsagePK.equals(other.tblPromotionUsagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TblPromotionUsage[ tblPromotionUsagePK=" + tblPromotionUsagePK + " ]";
    }
    
}
