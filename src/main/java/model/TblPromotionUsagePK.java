/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Embeddable
public class TblPromotionUsagePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    private int userId;
    @Basic(optional = false)
    @NotNull
    private int promotionId;

    public TblPromotionUsagePK() {
    }

    public TblPromotionUsagePK(int userId, int promotionId) {
        this.userId = userId;
        this.promotionId = promotionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) promotionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblPromotionUsagePK)) {
            return false;
        }
        TblPromotionUsagePK other = (TblPromotionUsagePK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.promotionId != other.promotionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TblPromotionUsagePK[ userId=" + userId + ", promotionId=" + promotionId + " ]";
    }
    
}
