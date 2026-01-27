package com.example.ecommerceproject.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReadAbleCategory {


    private Long id;
    private String code;
    private String categoryImage;
    private Integer sortOrder;
    private boolean categoryStatus;
    private boolean visible;
    private Integer depth;
    private String lineage;
    private boolean featured;
    private boolean isActive;
    private List<ReadAbleProduct> products;

    private List<ReadAbleCategoryDescription> categoryDescriptions;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getLineage() {
        return lineage;
    }

    public void setLineage(String lineage) {
        this.lineage = lineage;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ReadAbleProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ReadAbleProduct> products) {
        this.products = products;
    }

    public List<ReadAbleCategoryDescription> getCategoryDescriptions() {
        return categoryDescriptions;
    }

    public void setCategoryDescriptions(List<ReadAbleCategoryDescription> categoryDescriptions) {
        this.categoryDescriptions = categoryDescriptions;
    }
}
