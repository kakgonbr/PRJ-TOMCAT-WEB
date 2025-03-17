package model;

public class ProductWrapper {
    private Integer id;
    private Integer shopId;
    private Integer categoryId;
    private String name;
    private String description;
    private PromotionWrapper promotion;
    private String thumbnailId;

    public ProductWrapper() {}

    public ProductWrapper(Product product) {
        setId(product.getId());
        setShopId(product.getShopId().getId());
        setCategoryId(product.getCategoryId().getId());
        setName(product.getName());
        setDescription(product.getDescription());
        setPromotion(product.getAvailablePromotionId() == null ? null : new PromotionWrapper(product.getAvailablePromotionId()));
        setThumbnailId(product.getImageStringResourceId().getId());
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PromotionWrapper getPromotion() {
        return promotion;
    }


    public Integer getShopId() {
        return shopId;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPromotion(PromotionWrapper promotion) {
        this.promotion = promotion;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }
}
