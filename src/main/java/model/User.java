package model;

public final class User {
    public static final class LinkStatus {
        private String googleId;
        private String facebookId;

        public LinkStatus() {
        }

        public LinkStatus(String t_googleId, String t_facebookId) {
            setFacebookId(t_facebookId);
            setGoogleId(t_googleId);
        }

        public String getFacebookId() {
            return facebookId;
        }

        public String getGoogleId() {
            return googleId;
        }

        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

        public void setGoogleId(String googleId) {
            this.googleId = googleId;
        }
    }

    private int id;
    private String username;
    private String displayName;
    private String profilePicResource;
    private String email; // Not visible to other users
    private String phoneNumber; // Not visible to other users
    private String password; // Only visible to system
    private LinkStatus linkStatus; // Not visible to other users
    private Cart cart; // Not visible to other users
    private String bio;
    private long credit;
    private String cookie;

    public User() {}

    // information only
    public User(int t_id, String t_username, String t_displayName, String t_profile, String t_bio, long t_credit) {
        setId(t_id);
        setUsername(t_username);
        setDisplayName(t_displayName);
        setProfilePicResource(t_profile);
        setBio(t_bio);
    }

    // everything
    public User(int t_id, String t_username, String t_displayName, String t_profile, String t_bio, long t_credit, String t_email, String t_phoneNumber, String t_password, LinkStatus t_linkStatus, Cart t_cart, String t_cookie) {
        this(t_id, t_username, t_displayName, t_profile, t_bio, t_credit);
        setEmail(t_email);
        setPhoneNumber(t_phoneNumber);
        setPassword(t_password);
        setLinkStatus(t_linkStatus);
        setCart(t_cart);
        setCookie(t_cookie);
    }

    public String getBio() {
        return bio;
    }

    public long getCredit() {
        return credit;
    }

    public Cart getCart() {
        return cart;
    }

    public String getCookie() {
        return cookie;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePicResource() {
        return profilePicResource;
    }

    public String getUsername() {
        return username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public void setCredit(long credit) {
        this.credit = credit;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePicResource(String profilePicResource) {
        this.profilePicResource = profilePicResource;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
