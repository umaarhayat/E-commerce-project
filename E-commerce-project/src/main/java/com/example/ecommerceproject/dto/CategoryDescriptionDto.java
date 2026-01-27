package com.example.ecommerceproject.dto;

public class CategoryDescriptionDto {
    private String description;
    private String descriptionType; // SHORT, LONG, SEO

    // getters & setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }
}
