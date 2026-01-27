package com.example.ecommerceproject.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_descriptions")
public class ProductDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Title of the product
    private String description; // Actual description
    private String descriptionType; // e.g., Short, Long, Technical
    private String language; // Language code, e.g., "en", "fr"

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionType() { return descriptionType; }
    public void setDescriptionType(String descriptionType) { this.descriptionType = descriptionType; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
