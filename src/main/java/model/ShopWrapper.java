package model;


public class ShopWrapper implements java.io.Serializable {
    private Integer id;
    private String name;
    private String profileResource;
    
    public ShopWrapper() {}

    public ShopWrapper(Shop shop) {
        setId(shop.getId());
        setName(shop.getName());
        setProfileResource(shop.getProfileStringResourceId().getId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileResource() {
        return profileResource;
    }

    public void setProfileResource(String profileResource) {
        this.profileResource = profileResource;
    }

    
}
