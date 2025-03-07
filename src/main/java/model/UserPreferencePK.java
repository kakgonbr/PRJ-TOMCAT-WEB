/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
@Embeddable
public class UserPreferencePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "userId")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "preference")
    private String preference;

    public UserPreferencePK() {
    }

    public UserPreferencePK(int userId, String preference) {
        this.userId = userId;
        this.preference = preference;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (preference != null ? preference.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPreferencePK)) {
            return false;
        }
        UserPreferencePK other = (UserPreferencePK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if ((this.preference == null && other.preference != null) || (this.preference != null && !this.preference.equals(other.preference))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserPreferencePK[ userId=" + userId + ", preference=" + preference + " ]";
    }
    
}
