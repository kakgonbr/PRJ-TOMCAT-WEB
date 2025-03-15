package model.dto;

import java.util.Date;

public class PromotionDTO implements java.io.Serializable {
    private Integer id;
    private String name;
    private int value;
    private Date expireDate;
    private Boolean type;
    private Boolean ofAdmin;
    private Date creationDate;
    private Integer creatorId;

    public PromotionDTO() {}

    public PromotionDTO(model.Promotion promotion) {
        setCreationDate(promotion.getCreationDate());
        setCreatorId(promotion.getCreatorId().getId());
        setExpireDate(promotion.getExpireDate());
        setId(promotion.getId());
        setName(promotion.getName());
        setOfAdmin(promotion.getOfAdmin());
        setType(promotion.getType());
        setValue(promotion.getValue());
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public Date getExpireDate() {
        return expireDate;
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

    public Date getCreationDate() {
        return creationDate;
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

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public void setOfAdmin(Boolean ofAdmin) {
        this.ofAdmin = ofAdmin;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}   