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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblChatBox")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatBox.findAll", query = "SELECT t FROM ChatBox t"),
    @NamedQuery(name = "ChatBox.findById", query = "SELECT t FROM ChatBox t WHERE t.id = :id")})
public class ChatBox implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "user1", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user1;
    @JoinColumn(name = "user2", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user2;
    @OneToMany(mappedBy = "chatBoxId")
    private List<ChatContent> tblChatBoxContentList;

    public ChatBox() {
    }

    public ChatBox(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @XmlTransient
    public List<ChatContent> getTblChatBoxContentList() {
        return tblChatBoxContentList;
    }

    public void setTblChatBoxContentList(List<ChatContent> tblChatBoxContentList) {
        this.tblChatBoxContentList = tblChatBoxContentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ChatBox)) {
            return false;
        }
        ChatBox other = (ChatBox) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TblChatBox[ id=" + id + " ]";
    }
    
}
