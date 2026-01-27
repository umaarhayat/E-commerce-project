package com.example.ecommerceproject.dto;

public class ProductDescriptionDto {
    private String name;              // Title of the product
    private String description;       // Actual description
    private String descriptionType;   // Type of description, e.g., Short, Long, Technical
    private String language;          // Language code, e.g., "en", "fr"

    // ================= Getters & Setters =================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
