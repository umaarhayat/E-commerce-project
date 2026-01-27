package com.example.ecommerceproject.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReadAbleProduct {

    private Long id;

    private String productImage;
    private String sku;
    private String refSku;
    private boolean available;
    private boolean active;
    private BigDecimal price;
    private int quantity;
    private Date dateAvailable;
    private Date createdAt;
    private Date updatedAt;

    // Only IDs
    private Long categoryId;
    private Long merchantStoreId;


    private List<ReadAbleProductDescription> productDescriptions;


    // ----- Getters & Setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getRefSku() { return refSku; }
    public void setRefSku(String refSku) { this.refSku = refSku; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

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

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getMerchantStoreId() { return merchantStoreId; }
    public void setMerchantStoreId(Long merchantStoreId) { this.merchantStoreId = merchantStoreId; }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<ReadAbleProductDescription> getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(List<ReadAbleProductDescription> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }
}
