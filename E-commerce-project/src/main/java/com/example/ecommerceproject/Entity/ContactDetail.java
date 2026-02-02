package com.example.ecommerceproject.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_details")
public class ContactDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorizedPersonName;
    private String designation;
    private String primaryMobileNumber;
    private String secondaryMobileNumber;
    private String primaryBusinessEmail;
    private String secondaryBusinessEmail;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    // getters & setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSecondaryMobileNumber() {
        return secondaryMobileNumber;
    }

    public void setSecondaryMobileNumber(String secondaryMobileNumber) {
        this.secondaryMobileNumber = secondaryMobileNumber;
    }

    public String getPrimaryBusinessEmail() {
        return primaryBusinessEmail;
    }

    public void setPrimaryBusinessEmail(String primaryBusinessEmail) {
        this.primaryBusinessEmail = primaryBusinessEmail;
    }

    public String getSecondaryBusinessEmail() {
        return secondaryBusinessEmail;
    }

    public void setSecondaryBusinessEmail(String secondaryBusinessEmail) {
        this.secondaryBusinessEmail = secondaryBusinessEmail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
