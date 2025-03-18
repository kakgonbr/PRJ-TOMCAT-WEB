package model;

public class PromotionWrapper implements java.io.Serializable {
    private Integer id;
    private String name;
    private Integer value;
    private String expireDate;
    private Boolean type; // 1 for fixed, 0 for percentage

    public PromotionWrapper() {}

    public PromotionWrapper(Promotion promotion) {
        setId(promotion.getId());
        setName(promotion.getName());
        setValue(promotion.getValue());
        setExpireDate(promotion.getExpireDate().toLocalDate().format(config.Config.Time.outputFormatDate));
        setType(promotion.getType());
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    
}
