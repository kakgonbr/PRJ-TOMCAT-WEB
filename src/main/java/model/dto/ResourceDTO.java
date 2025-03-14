package model.dto;

public class ResourceDTO implements java.io.Serializable {
    private String systemPath;
    private String id;

    public ResourceDTO() {}

    public ResourceDTO(model.ResourceMap resourceMap) {
        setId(resourceMap.getId());
        setSystemPath(resourceMap.getSystemPath());
    }

    public model.ResourceMap toResourceMap() {
        model.ResourceMap resourceMap = new model.ResourceMap();

        resourceMap.setSystemPath(systemPath);
        resourceMap.setId(id);

        return resourceMap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }
    
    public String getId() {
        return id;
    }

    public String getSystemPath() {
        return systemPath;
    }
}