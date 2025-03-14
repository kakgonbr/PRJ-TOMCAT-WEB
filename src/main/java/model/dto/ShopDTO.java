package model.dto;

public  class ShopDTO implements java.io.Serializable {
    private String name;
    private String address;
    private Integer id;
    private Boolean visible;
    private Integer ownerId;

    public ShopDTO() {}

    public ShopDTO(model.Shop shop) {
        setAddress(shop.getAddress());
        setId(shop.getId());
        setName(shop.getName());
        setOwnerId(shop.getOwnerId().getId());
        setVisible(shop.getVisible());
    }

    public model.Shop toShop() {
        model.Shop shop = new model.Shop();

        shop.setName(name);
        shop.setAddress(address);
        shop.setId(id);
        shop.setVisible(visible);
        shop.setOwnerId(new model.User(ownerId));

        return shop;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
