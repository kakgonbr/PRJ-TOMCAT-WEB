package model;

/**
 * Used for displaying a detailed product information
 */
public class ProductDetailsWrapper implements java.io.Serializable {
    private Integer id;
    private ShopWrapper shop;
    private CategoryWrapper category;
    private String name;
    private String description;
    private PromotionWrapper promotion;
    private String thumbnail;
    private java.util.List<String> productImages;
    private java.util.List<ProductCustomizationWrapper> productCustomizations;
    private String customizationName;

    public ProductDetailsWrapper() {}

    public ProductDetailsWrapper(Product product) {
        setId(product.getId());
        setShop(new ShopWrapper(product.getShopId()));
        setCategory(new CategoryWrapper(product.getCategoryId(), false));
        setName(product.getName());
        setDescription(product.getDescription());
        setPromotion(product.getAvailablePromotionId() == null ? null : new PromotionWrapper(product.getAvailablePromotionId()));
        setThumbnail(product.getImageStringResourceId().getId());
        setProductImages(product.getProductImageList().stream().map(ProductImage::getImageStringResourceId).map(ResourceMap::getId).toList());
        // product item and product customization are one to one

        try {
            setProductCustomizations(product.getProductItemList().stream().map(ProductItem::getProductCustomizationList).map(l -> l.get(0)).map(ProductCustomizationWrapper::new).toList());
        } catch (IndexOutOfBoundsException e) {
            // No way this happens. For each product item there must be one customization. The database doesn't really reflect this, but it's a little too late for change
        }

        if (productCustomizations.size() != 0) {
            setCustomizationName(productCustomizations.get(0).getName());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShopWrapper getShop() {
        return shop;
    }

    public void setShop(ShopWrapper shop) {
        this.shop = shop;
    }

    public CategoryWrapper getCategory() {
        return category;
    }

    public void setCategory(CategoryWrapper category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PromotionWrapper getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionWrapper promotion) {
        this.promotion = promotion;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public java.util.List<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(java.util.List<String> productImages) {
        this.productImages = productImages;
    }

    public java.util.List<ProductCustomizationWrapper> getProductCustomizations() {
        return productCustomizations;
    }

    public void setProductCustomizations(java.util.List<ProductCustomizationWrapper> productCustomizations) {
        this.productCustomizations = productCustomizations;
    }

    public String getCustomizationName() {
        return customizationName;
    }

    public void setCustomizationName(String customizationName) {
        this.customizationName = customizationName;
    }

    
}
