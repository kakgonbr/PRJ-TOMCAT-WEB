package model.dto;

public class PromotionDTO implements java.io.Serializable {
    private Integer id;
    private String name;
    private int value;
    private String expireDate;
    private Boolean type;
    private Boolean ofAdmin;
    private String creationDate;
    private Integer creatorId;
    private Boolean status;

    public PromotionDTO() {}

    public PromotionDTO(model.Promotion promotion) {
        setCreationDate(promotion.getCreationDate().toLocalDate().format(config.Config.Time.outputFormatDate));
        setCreatorId(promotion.getCreatorId().getId());
        setExpireDate(promotion.getExpireDate().toLocalDate().format(config.Config.Time.outputFormatDate));
        setId(promotion.getId());
        setName(promotion.getName());
        setOfAdmin(promotion.getOfAdmin());
        setType(promotion.getType());
        setValue(promotion.getValue());
        setStatus(promotion.getStatus());
    }

    public model.Promotion toPromotion() {
        model.Promotion promotion = new model.Promotion();

        promotion.setCreationDate(java.sql.Date.valueOf(java.time.LocalDate.parse(creationDate, config.Config.Time.inputFormatDate))); // shouldnt be modified? I mean it's the admin, they have the rights to.
        promotion.setExpireDate(java.sql.Date.valueOf(java.time.LocalDate.parse(expireDate, config.Config.Time.inputFormatDate)));
        promotion.setCreatorId(new model.User(creatorId));
        promotion.setName(name);
        promotion.setValue(value);
        promotion.setType(type);
        promotion.setOfAdmin(ofAdmin);
        promotion.setStatus(status);

        return promotion;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getType() {
        return type;
    }

    public Boolean getOfAdmin() {
        return ofAdmin;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public void setOfAdmin(Boolean ofAdmin) {
        this.ofAdmin = ofAdmin;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}   