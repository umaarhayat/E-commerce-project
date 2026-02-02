package com.example.ecommerceproject.dto;

public class ReadAbleContactDetail {

    private String authorizedPersonName;
    private String designation;
    private String primaryMobileNumber;
    private String primaryBusinessEmail;

    // getters & setters


    public String getAuthorizedPersonName() {
        return authorizedPersonName;
    }

    public void setAuthorizedPersonName(String authorizedPersonName) {
        this.authorizedPersonName = authorizedPersonName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPrimaryMobileNumber() {
        return primaryMobileNumber;
    }

    public void setPrimaryMobileNumber(String primaryMobileNumber) {
        this.primaryMobileNumber = primaryMobileNumber;
    }

    public String getPrimaryBusinessEmail() {
        return primaryBusinessEmail;
    }

    public void setPrimaryBusinessEmail(String primaryBusinessEmail) {
        this.primaryBusinessEmail = primaryBusinessEmail;
    }
}
