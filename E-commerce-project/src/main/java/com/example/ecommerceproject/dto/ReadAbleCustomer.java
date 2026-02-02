package com.example.ecommerceproject.dto;

import java.util.List;

public class ReadAbleCustomer {

    private Long id;
    private String companyName;
    private String companyTaxNumber;
    private String businessType;
    private String customerType;
    private String companyWebsite;
    private List<ReadAbleContactDetail> contactDetails;

    // getters & setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTaxNumber() {
        return companyTaxNumber;
    }

    public void setCompanyTaxNumber(String companyTaxNumber) {
        this.companyTaxNumber = companyTaxNumber;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public List<ReadAbleContactDetail> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(List<ReadAbleContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }
}
