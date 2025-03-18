package model;

public class ProductWrapper {
    private Integer id;
    private ShopWrapper shop;
    private CategoryWrapper category;
    private String name;
    private String description;
    private PromotionWrapper promotion;
    private String thumbnail;

    public ProductWrapper() {}

    public ProductWrapper(Product product) {
        setId(product.getId());
        // setShopId(product.getShopId().getId());
        // setCategoryId(product.getCategoryId().getId());
        setShop(new ShopWrapper(product.getShopId()));
        setCategory(new CategoryWrapper(product.getCategoryId(), false));
        setName(product.getName());
        setDescription(product.getDescription());
        setPromotion(product.getAvailablePromotionId() == null ? null : new PromotionWrapper(product.getAvailablePromotionId()));
        setThumbnail(product.getImageStringResourceId().getId());
    }

    public ShopWrapper getShop() {
        return shop;
    }

    public void setShop(ShopWrapper shopId) {
        this.shop = shopId;
    }

    public CategoryWrapper getCategory() {
        return category;
    }

    public void setCategory(CategoryWrapper categoryId) {
        this.category = categoryId;
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

    public String getThumbnail() {
        return thumbnail;
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

    public void setThumbnail(String thumbnailId) {
        this.thumbnail = thumbnailId;
    }
}
