/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblChatBoxContent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatBoxContent.findAll", query = "SELECT t FROM ChatBoxContent t"),
    @NamedQuery(name = "ChatBoxContent.findById", query = "SELECT t FROM ChatBoxContent t WHERE t.id = :id"),
    @NamedQuery(name = "ChatBoxContent.findByMessage", query = "SELECT t FROM ChatBoxContent t WHERE t.message = :message"),
    @NamedQuery(name = "ChatBoxContent.findByTime", query = "SELECT t FROM ChatBoxContent t WHERE t.time = :time")})
public class ChatContent implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "chatBoxId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ChatBox chatBoxId;
    @JoinColumn(name = "senderId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User senderId;

    public ChatContent() {
    }

    public ChatContent(Integer id) {
        this.id = id;
    }

    public ChatContent(Integer id, String message, Date time) {
        this.id = id;
        this.message = message;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public ChatBox getChatBoxId() {
        return chatBoxId;
    }

    public void setChatBoxId(ChatBox chatBoxId) {
        this.chatBoxId = chatBoxId;
    }

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
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
        if (!(object instanceof ChatContent)) {
            return false;
        }
        ChatContent other = (ChatContent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TblChatBoxContent[ id=" + id + " ]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
}
