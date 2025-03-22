/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "tblUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPhoneNumber", query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByPersistentCookie", query = "SELECT u FROM User u WHERE u.persistentCookie = :persistentCookie"),
    @NamedQuery(name = "User.findByGoogleId", query = "SELECT u FROM User u WHERE u.googleId = :googleId"),
    @NamedQuery(name = "User.findByFacebookId", query = "SELECT u FROM User u WHERE u.facebookId = :facebookId"),
    @NamedQuery(name = "User.findByIsAdmin", query = "SELECT u FROM User u WHERE u.isAdmin = :isAdmin"),
    @NamedQuery(name = "User.findByCredit", query = "SELECT u FROM User u WHERE u.credit = :credit"),
    @NamedQuery(name = "User.findByDisplayName", query = "SELECT u FROM User u WHERE u.displayName = :displayName"),
    @NamedQuery(name = "User.findByBio", query = "SELECT u FROM User u WHERE u.bio = :bio")})
public class User implements Serializable {

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 30)
    @Column(name = "username")
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "persistentCookie")
    private String persistentCookie;
    @Size(max = 255)
    @Column(name = "googleId")
    private String googleId;
    @Size(max = 255)
    @Column(name = "facebookId")
    private String facebookId;
    @Size(max = 50)
    @Column(name = "displayName")
    private String displayName;
    @Size(max = 255)
    @Column(name = "bio")
    private String bio;
    @OneToMany(mappedBy = "user1")
    private List<ChatBox> chatBoxList;
    @OneToMany(mappedBy = "user2")
    private List<ChatBox> chatBoxList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<Preference> preferenceList;
    @OneToMany(mappedBy = "userId")
    private List<Notification> notificationList;
    @OneToMany(mappedBy = "userId")
    private List<Cart> cartList;
    @OneToMany(mappedBy = "senderId")
    private List<ChatContent> chatContentList;
    @OneToMany(mappedBy = "userId")
    private List<ProductOrder> productOrderList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserPreference> userPreferenceList;
    @OneToMany(mappedBy = "userId")
    private List<Review> reviewList;
    @OneToMany(mappedBy = "userId")
    private List<OnholdCredit> onholdCreditList;
    @OneToMany(mappedBy = "ownerId")
    private Collection<Shop> shopCollection;
    @OneToMany(mappedBy = "creatorId")
    private Collection<Promotion> promotionCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "isAdmin")
    private Boolean isAdmin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @NotNull
    @Column(name = "credit")
    private BigDecimal credit;
    @JoinColumn(name = "profileStringResourceId", referencedColumnName = "id")
    @ManyToOne
    private ResourceMap profileStringResourceId;
    @Column(name = "status")
    private Boolean status;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String email, String username, String phoneNumber, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }


    public ResourceMap getProfileStringResourceId() {
        return profileStringResourceId;
    }

    public void setProfileStringResourceId(ResourceMap profileStringResourceId) {
        this.profileStringResourceId = profileStringResourceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.User[ id=" + id + " ]";
    }

    
    public boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Shop> getShopCollection() {
        return shopCollection;
    }

    public void setShopCollection(Collection<Shop> shopCollection) {
        this.shopCollection = shopCollection;
    }

    @XmlTransient
    public Collection<Promotion> getPromotionCollection() {
        return promotionCollection;
    }

    public void setPromotionCollection(Collection<Promotion> promotionCollection) {
        this.promotionCollection = promotionCollection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersistentCookie() {
        return persistentCookie;
    }

    public void setPersistentCookie(String persistentCookie) {
        this.persistentCookie = persistentCookie;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @XmlTransient
    public List<ChatBox> getChatBoxList() {
        return chatBoxList;
    }

    public void setChatBoxList(List<ChatBox> chatBoxList) {
        this.chatBoxList = chatBoxList;
    }

    @XmlTransient
    public List<ChatBox> getChatBoxList1() {
        return chatBoxList1;
    }

    public void setChatBoxList1(List<ChatBox> chatBoxList1) {
        this.chatBoxList1 = chatBoxList1;
    }

    @XmlTransient
    public List<Preference> getPreferenceList() {
        return preferenceList;
    }

    public void setPreferenceList(List<Preference> preferenceList) {
        this.preferenceList = preferenceList;
    }

    @XmlTransient
    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @XmlTransient
    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @XmlTransient
    public List<ChatContent> getChatContentList() {
        return chatContentList;
    }

    public void setChatContentList(List<ChatContent> chatContentList) {
        this.chatContentList = chatContentList;
    }

    @XmlTransient
    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrder> productOrderList) {
        this.productOrderList = productOrderList;
    }

    @XmlTransient
    public List<UserPreference> getUserPreferenceList() {
        return userPreferenceList;
    }

    public void setUserPreferenceList(List<UserPreference> userPreferenceList) {
        this.userPreferenceList = userPreferenceList;
    }

    @XmlTransient
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @XmlTransient
    public List<OnholdCredit> getOnholdCreditList() {
        return onholdCreditList;
    }

    public void setOnholdCreditList(List<OnholdCredit> onholdCreditList) {
        this.onholdCreditList = onholdCreditList;
    }
    
}
