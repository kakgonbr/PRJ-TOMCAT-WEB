package model;

public class ProductWrapper {
    private Integer id;
    private Integer shopId;
    private Integer categoryId;
    private String name;
    private String description;
    private Integer promotionId;
    private String thumbnailId;

    public ProductWrapper() {}

    public ProductWrapper(Product product) {
        setId(product.getId());
        setShopId(product.getShopId().getId());
        setCategoryId(product.getCategoryId().getId());
        setName(product.getName());
        setDescription(product.getDescription());
        setPromotionId(product.getAvailablePromotionId() == null ? null : product.getAvailablePromotionId().getId());
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

    public Integer getPromotionId() {
        return promotionId;
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

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public static ProductOld fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ProductOld(rs.getInt("id"),
        rs.getInt("shopId"),
        rs.getInt("categoryId"),
        rs.getString("name"),
        rs.getString("description"),
        rs.getInt("promotionId"),
        rs.getString("thumbnailId"));
    }
}
