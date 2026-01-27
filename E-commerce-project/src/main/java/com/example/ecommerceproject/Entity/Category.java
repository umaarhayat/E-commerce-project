package com.example.ecommerceproject.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "category_image")
    private String categoryImage;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "category_status")
    private Boolean categoryStatus = false;
    @Column(name = "visible")
    private Boolean visible = true;
    @Column(name = "depth")
    private Integer depth;
    @Column(name = "lineage")
    private String lineage;
    @Column(name = "featured")
    private Boolean featured = false;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> products;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryDescription> categoryDescriptions = new ArrayList<>();

    // ================= GETTERS / SETTERS =================

    public Long getId() {
        return id;
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

    public Boolean getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(Boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
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

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<CategoryDescription> getCategoryDescriptions() {
        return categoryDescriptions;
    }

    public void setCategoryDescriptions(List<CategoryDescription> categoryDescriptions) {
        this.categoryDescriptions = categoryDescriptions;
    }
}
