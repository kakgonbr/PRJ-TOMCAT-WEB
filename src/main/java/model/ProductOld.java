package model;

public class ProductOld {
    private int id;
    private int shopId;
    private int categoryId;
    private String name;
    private String description;
    private int promotionId;
    private String thumbnailId;

    public ProductOld() {}

    public ProductOld(int t_id, int t_shop, int t_category, String t_name, String t_description, int t_promo, String t_thumbnail) {
        setCategoryId(t_category);
        setDescription(t_description);
        setId(t_id);
        setName(t_name);
        setPromotionId(t_promo);
        setShopId(t_shop);
        setThumbnailId(t_thumbnail);   
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public int getShopId() {
        return shopId;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public void setShopId(int shopId) {
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
