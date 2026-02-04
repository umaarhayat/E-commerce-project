package com.example.ecommerceproject.dto;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Entity.MerchantStore;
import org.jetbrains.annotations.NotNull;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductDto {

    private Long id;
    private  String productName;
    private String productImage;
    private String sku;
    private String refSku;
    private boolean available;
    private boolean isActive;
    private BigDecimal price;
    private int quantity;
    private Date dateAvailable;
    private Date createdAt;
    private Date updatedAt;
    private Long categoryId;
    private String categoryName;
    private Long merchantStoreId;
    private String storeCode;
    private String storeName;

    private MerchantStore merchantStore;
    private Category category;


    private List<ReadAbleProductDescription> productDescriptions;



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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getMerchantStoreId() {
        return merchantStoreId;
    }

    public void setMerchantStoreId(Long merchantStoreId) {
        this.merchantStoreId = merchantStoreId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<ReadAbleProductDescription> getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(List<ReadAbleProductDescription> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
