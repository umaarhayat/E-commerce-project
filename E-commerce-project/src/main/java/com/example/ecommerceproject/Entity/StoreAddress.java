package com.example.ecommerceproject.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Entity
    @Table(name = "store_addresses")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class StoreAddress {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String country;

        @Column(nullable = false)
        private String city;

        @Column(nullable = false)
        private String postalCode;

        @Column(nullable = false)
        private String address;

        // 🔗 Relationship with MerchantStore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "merchant_store_id", nullable = false)
        private MerchantStore merchantStore;

        public Long getId() {

            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public MerchantStore getMerchantStore() {
            return merchantStore;
        }

        public void setMerchantStore(MerchantStore merchantStore) {
            this.merchantStore = merchantStore;
        }
    }


