package model.dto;

public class ProductDTO implements java.io.Serializable {
    private Integer id;
    private String name;
    private String description;
    private Integer categoryId;
    private Integer availablePromotionId;
    private String imageStringResourceId;
    private Integer shopId;

    public ProductDTO() {}

    public ProductDTO(model.Product product) {
        setAvailablePromotionId(product.getAvailablePromotionId() == null ? null : product.getAvailablePromotionId().getId());
        setCategoryId(product.getCategoryId().getId());
        setDescription(product.getDescription());
        setId(product.getId());
        setImageStringResourceId(product.getImageStringResourceId().getId());
        setName(product.getName());
        setShopId(product.getShopId().getId());
    }

    public model.Product toProduct() {
        model.Product product = new model.Product();

        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        // DANGER: NOT GETTING REFERENCE USING THE ENTITY MANAGER HERE, MAKE SURE THIS DOESN'T SCREW UP ANYTHING
        product.setCategoryId(new model.Category(getCategoryId()));
        product.setAvailablePromotionId(new model.Promotion(getAvailablePromotionId()));
        product.setImageStringResourceId(new model.ResourceMap(getImageStringResourceId()));

        return product;
    }

    public void setAvailablePromotionId(Integer availablePromotionId) {
        this.availablePromotionId = availablePromotionId;
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

    public void setImageStringResourceId(String imageStringResourceId) {
        this.imageStringResourceId = imageStringResourceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
    
    public Integer getAvailablePromotionId() {
        return availablePromotionId;
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

    public String getImageStringResourceId() {
        return imageStringResourceId;
    }

    public String getName() {
        return name;
    }

    public Integer getShopId() {
        return shopId;
    }
}