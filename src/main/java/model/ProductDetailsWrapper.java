package model;

import java.util.stream.Collectors;

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
    private java.util.List<ProductItemWrapper> productItems;
    private java.util.Set<String> customizationNames;

    public ProductDetailsWrapper() {}

    public ProductDetailsWrapper(Product product) {
        setId(product.getId());
        setShop(new ShopWrapper(product.getShopId()));
        setCategory(new CategoryWrapper(product.getCategoryId(), false));
        setName(product.getName());
        setDescription(product.getDescription());
        setPromotion(product.getAvailablePromotionId() == null ? null : new PromotionWrapper(product.getAvailablePromotionId()));
        setThumbnail(product.getImageStringResourceId() == null ? null : product.getImageStringResourceId().getId());
        
        // way too java-y
        setProductImages(product.getProductImageList().stream().map(ProductImage::getImageStringResourceId).map(ResourceMap::getId).toList());
        setProductItems(product.getProductItemList().stream().map(ProductItemWrapper::new).toList());
        setCustomizationNames(productItems.stream().flatMap(i -> i.getCustomizations().stream()).map(ProductCustomizationWrapper::getName).collect(Collectors.toSet()));
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

    public java.util.Set<String> getCustomizationNames() {
        return customizationNames;
    }

    public void setCustomizationNames(java.util.Set<String> customizationNames) {
        this.customizationNames = customizationNames;
    }

    public java.util.List<ProductItemWrapper> getProductItems() {
        return productItems;
    }

    public void setProductItems(java.util.List<ProductItemWrapper> productItems) {
        this.productItems = productItems;
    }    
}
