package model.dto;

import java.math.BigDecimal;

public class UserDTO implements java.io.Serializable {
    private Integer id;
    private String email;
    private String username;
    private String phoneNumber;
    private String password;
    private String persistentCookie;
    private String googleId;
    private String facebookId;
    private String displayName;
    private String bio;
    private Boolean isAdmin;
    private Long credit;
    private String profileStringResourceId;
    private Boolean status;
    

    public UserDTO() {}

    public UserDTO(model.User user) {
        setBio(user.getBio());
        setCredit(user.getCredit() == null ? null : user.getCredit().longValue());
        setDisplayName(user.getDisplayName());
        setEmail(user.getEmail());
        setFacebookId(user.getFacebookId());
        setGoogleId(user.getGoogleId());
        setId(user.getId());
        setIsAdmin(user.getIsAdmin());
        setPassword(user.getPassword());
        setPersistentCookie(user.getPersistentCookie());
        setPhoneNumber(user.getPhoneNumber());
        setProfileStringResourceId(user.getProfileStringResourceId() == null ? null : user.getProfileStringResourceId().getId());
        setStatus(user.isStatus());
        setUsername(user.getUsername());
    }

    public model.User toUser() {
        model.User user = new model.User();
        user.setId(id);
        user.setEmail(email);
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setPersistentCookie(persistentCookie);
        user.setGoogleId(googleId);
        user.setFacebookId(facebookId);
        user.setDisplayName(displayName);
        user.setBio(bio);
        user.setIsAdmin(isAdmin);
        user.setCredit(BigDecimal.valueOf(credit));
        user.setProfileStringResourceId(profileStringResourceId == null ? null : new model.ResourceMap(profileStringResourceId));
        user.setStatus(status);

        return user;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getPersistentCookie() {
        return persistentCookie;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public Long getCredit() {
        return credit;
    }

    public String getProfileStringResourceId() {
        return profileStringResourceId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPersistentCookie(String persistentCookie) {
        this.persistentCookie = persistentCookie;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public void setProfileStringResourceId(String profileStringResourceId) {
        this.profileStringResourceId = profileStringResourceId;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
