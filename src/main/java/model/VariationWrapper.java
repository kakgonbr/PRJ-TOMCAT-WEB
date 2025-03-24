/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class VariationWrapper implements java.io.Serializable {
    private int id;
    private String name;
    private String datatype;
    private String unit;
    // private List<VariationValueWrapper> values;

    public VariationWrapper() {}


    public VariationWrapper(Variation variation) {
        this.id = variation.getId();
        this.name = variation.getName();
        this.datatype = variation.getDatatype();
        this.unit = variation.getUnit();
        
        // Chuyển đổi danh sách VariationValue sang VariationValueWrapper
        // // this.values = variation.getVariationValueList()
        //                        .stream()
        //                        .map(VariationValueWrapper::new)
        //                        .collect(Collectors.toList());
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDatatype() { return datatype; }
    public void setDatatype(String datatype) { this.datatype = datatype; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    // public List<VariationValueWrapper> getValues() { return values; }
    // public void setValues(List<VariationValueWrapper> values) { this.values = values; }
}

