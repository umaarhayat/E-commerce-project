package com.example.ecommerceproject.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_image")
    private String productImage;
    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "ref_sku")
    private String refSku;

    @Column(name = "available")
    private boolean available;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "date_available")
    @Temporal(TemporalType.DATE)
    private Date dateAvailable;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    // ----- Associations -----
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "merchant_store_id")
    private MerchantStore merchantStore;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDescription> productDescriptions;


    // ----- Getters & Setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getRefSku() { return refSku; }
    public void setRefSku(String refSku) { this.refSku = refSku; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getDateAvailable() { return dateAvailable; }
    public void setDateAvailable(Date dateAvailable) { this.dateAvailable = dateAvailable; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public MerchantStore getMerchantStore() { return merchantStore; }
    public void setMerchantStore(MerchantStore merchantStore) { this.merchantStore = merchantStore; }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<ProductDescription> getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(List<ProductDescription> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }
}
