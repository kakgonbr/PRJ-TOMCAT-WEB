package model;

/**
 * For now, this is only used to display a list of filters
 */
public class CategoryWrapper {
    private int id;
    private String name;

    public CategoryWrapper() {}
    
    public CategoryWrapper(model.Category category) {
        setId(category.getId());
        setName(category.getName());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
