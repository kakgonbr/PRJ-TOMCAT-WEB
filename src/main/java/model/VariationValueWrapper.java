/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class VariationValueWrapper implements java.io.Serializable {
    private int id;
    private String value;

    public VariationValueWrapper() {}

    public VariationValueWrapper(VariationValue variationValue) {
        this.id = variationValue.getId();
        this.value = variationValue.getValue();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
