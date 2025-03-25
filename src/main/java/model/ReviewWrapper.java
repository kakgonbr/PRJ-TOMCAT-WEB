package model;

public class ReviewWrapper implements java.io.Serializable{
    private int id, rate, userId, productId;
    private String comment, userName, profileStringResourceId;
    
    public ReviewWrapper() {
    }

    public ReviewWrapper(Review review) {
        this.id = review.getId();
        this.rate = review.getRate();
        this.userId = review.getUserId().getId();
        this.productId = review.getProductId().getId();
        this.comment = review.getComment();
        this.userName = review.getUserId().getDisplayName() == null ? review.getUserId().getUsername() : review.getUserId().getDisplayName();
        this.profileStringResourceId = review.getUserId().getProfileStringResourceId().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileStringResourceId() {
        return profileStringResourceId;
    }

    public void setProfileStringResourceId(String profileStringResourceId) {
        this.profileStringResourceId = profileStringResourceId;
    }
    
}
