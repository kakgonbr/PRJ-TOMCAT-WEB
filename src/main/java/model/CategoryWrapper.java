package model;

import java.util.stream.Collectors;

/**
 * For now, this is only used to display a list of filters
 */
public class CategoryWrapper implements java.io.Serializable {
    private int id;
    private String name;
    // private int parent;
    private java.util.List<CategoryWrapper> children;

    public CategoryWrapper() {}
    
    public CategoryWrapper(Category category) {
        setId(category.getId());
        setName(category.getName());
        // setParent(category.getParentId() == null ? null : category.getParentId().getId());
        java.util.List<Category> categories = category.getCategoryList();
        children = categories.stream().map(CategoryWrapper::new).collect(Collectors.toList());
    }

    // public void setParent(int parent) {
    //     this.parent = parent;
    // }

    // public int getParent() {
    //     return parent;
    // }

    public java.util.List<CategoryWrapper> getChildren() {
        return children;
    }

    public void setChildren(CategoryWrapper category) {
        children.add(category);
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
