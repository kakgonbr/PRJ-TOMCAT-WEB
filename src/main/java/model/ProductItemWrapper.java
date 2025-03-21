package model;

public class ProductItemWrapper {
    private Integer id;
    private Integer stock;
    private Long price;
    private java.util.List<ProductCustomizationWrapper> customizations;
    
    public ProductItemWrapper() {}

    public ProductItemWrapper(ProductItem productItem) {
        setId(productItem.getId());
        setStock(productItem.getStock());
        setPrice(productItem.getPrice() == null ? null : productItem.getPrice().longValue());
        setCustomizations(productItem.getProductCustomizationList().stream().map(ProductCustomizationWrapper::new).toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public java.util.List<ProductCustomizationWrapper> getCustomizations() {
        return customizations;
    }

    public void setCustomizations(java.util.List<ProductCustomizationWrapper> customizations) {
        this.customizations = customizations;
    }
    
}
