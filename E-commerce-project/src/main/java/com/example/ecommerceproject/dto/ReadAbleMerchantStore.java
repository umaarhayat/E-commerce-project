package com.example.ecommerceproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadAbleMerchantStore {

    private Long id;
    private String storeName;
    private String description;
    private String logo;
    private String logoUrl;
    private String ownerName;
    private String ownerEmail;
    private String phone;
    private String country;
    private String city;
    private String address;
    private Boolean isDelete = false;
    private Boolean isActive = true; // default true




    private ReadAbleUser readAbleUser;

    private List<ReadAbleStoreAddress> storeAddresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ReadAbleUser getReadAbleUser() {
        return readAbleUser;
    }

    public void setReadAbleUser(ReadAbleUser readAbleUser) {
        this.readAbleUser = readAbleUser;
    }

    public List<ReadAbleStoreAddress> getStoreAddresses() {
        return storeAddresses;
    }

    public void setStoreAddresses(List<ReadAbleStoreAddress> storeAddresses) {
        this.storeAddresses = storeAddresses;
    }


    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
