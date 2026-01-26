package com.example.ecommerceproject.dto;

import org.jetbrains.annotations.NotNull;


import java.math.BigDecimal;
import java.util.Date;

public class ProductDto {

    private Long id;


    private String sku;

    private String refSku;
    private boolean available;
    private boolean isActive;

    private BigDecimal price;

    private int quantity;
    private Date dateAvailable;

    private Long categoryId;

    private Long merchantStoreId;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getMerchantStoreId() {
        return merchantStoreId;
    }

    public void setMerchantStoreId(Long merchantStoreId) {
        this.merchantStoreId = merchantStoreId;
    }
}
