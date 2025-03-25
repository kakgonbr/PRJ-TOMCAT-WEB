package model;

public class ReviewWrapper implements java.io.Serializable{
    private int id, rate, userId, productId;
    private String comment;
    
    public ReviewWrapper() {
    }

    public ReviewWrapper(Review review) {
        this.id = review.getId();
        this.rate = review.getRate();
        this.userId = review.getUserId().getId();
        this.productId = review.getProductId().getId();
        this.comment = review.getComment();
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
    
}
