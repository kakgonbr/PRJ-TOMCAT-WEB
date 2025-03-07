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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblOnholdCredit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OnholdCredit.findAll", query = "SELECT o FROM OnholdCredit o"),
    @NamedQuery(name = "OnholdCredit.findById", query = "SELECT o FROM OnholdCredit o WHERE o.id = :id"),
    @NamedQuery(name = "OnholdCredit.findByAmount", query = "SELECT o FROM OnholdCredit o WHERE o.amount = :amount"),
    @NamedQuery(name = "OnholdCredit.findByDate", query = "SELECT o FROM OnholdCredit o WHERE o.date = :date"),
    @NamedQuery(name = "OnholdCredit.findByClaimDate", query = "SELECT o FROM OnholdCredit o WHERE o.claimDate = :claimDate")})
public class OnholdCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "claimDate")
    @Temporal(TemporalType.DATE)
    private Date claimDate;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public OnholdCredit() {
    }

    public OnholdCredit(Integer id) {
        this.id = id;
    }

    public OnholdCredit(Integer id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof OnholdCredit)) {
            return false;
        }
        OnholdCredit other = (OnholdCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.OnholdCredit[ id=" + id + " ]";
    }
    
}
