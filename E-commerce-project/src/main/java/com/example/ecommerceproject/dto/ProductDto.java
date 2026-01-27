package com.example.ecommerceproject.dto;

import org.jetbrains.annotations.NotNull;


import java.math.BigDecimal;
import java.util.Date;

public class ProductDto {

    private Long id;
    private String productImage;
    private String sku;

    private String refSku;
    private boolean available;
    private boolean isActive;

    private BigDecimal price;
    private int quantity;
    private Date dateAvailable;

    private String categoryId;
    private String merchantStoreId;

    // Getters & Setters
    // ... (generate all)


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRefSku() {
        return refSku;
    }

    public void setRefSku(String refSku) {
        this.refSku = refSku;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(Date dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMerchantStoreId() {
        return merchantStoreId;
    }

    public void setMerchantStoreId(String merchantStoreId) {
        this.merchantStoreId = merchantStoreId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
