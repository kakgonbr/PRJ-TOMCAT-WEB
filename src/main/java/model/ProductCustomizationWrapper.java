package model;

public class ProductCustomizationWrapper implements java.io.Serializable {
    private Integer id;
    private String name;
    private Integer stock;
    private Long price;
    private String value;
    private String unit;

    public ProductCustomizationWrapper() {}

    public ProductCustomizationWrapper(ProductCustomization productCustomization) {
        setId(productCustomization.getId());
        setStock(productCustomization.getProductItemId().getStock());
        setPrice(productCustomization.getProductItemId().getPrice() == null ? null : productCustomization.getProductItemId().getPrice().longValue());
        setName(productCustomization.getVariationValueId().getVariationId().getName());
        setUnit(productCustomization.getVariationValueId().getVariationId().getUnit());
        setValue(productCustomization.getVariationValueId().getValue());   
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
}
