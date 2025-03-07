/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table(name = "tblUserPreference")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserPreference.findAll", query = "SELECT u FROM UserPreference u"),
    @NamedQuery(name = "UserPreference.findByUserId", query = "SELECT u FROM UserPreference u WHERE u.userPreferencePK.userId = :userId"),
    @NamedQuery(name = "UserPreference.findByPreference", query = "SELECT u FROM UserPreference u WHERE u.userPreferencePK.preference = :preference")})
public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserPreferencePK userPreferencePK;
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public UserPreference() {
    }

    public UserPreference(UserPreferencePK userPreferencePK) {
        this.userPreferencePK = userPreferencePK;
    }

    public UserPreference(int userId, String preference) {
        this.userPreferencePK = new UserPreferencePK(userId, preference);
    }

    public UserPreferencePK getUserPreferencePK() {
        return userPreferencePK;
    }

    public void setUserPreferencePK(UserPreferencePK userPreferencePK) {
        this.userPreferencePK = userPreferencePK;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userPreferencePK != null ? userPreferencePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPreference)) {
            return false;
        }
        UserPreference other = (UserPreference) object;
        if ((this.userPreferencePK == null && other.userPreferencePK != null) || (this.userPreferencePK != null && !this.userPreferencePK.equals(other.userPreferencePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserPreference[ userPreferencePK=" + userPreferencePK + " ]";
    }
    
}
