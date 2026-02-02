package com.example.ecommerceproject.specification.impl;

import com.example.ecommerceproject.Entity.MerchantStore;
import com.example.ecommerceproject.specification.MerchantStoreSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class MerchantStoreSpecificationImpl implements MerchantStoreSpecification {
    public Specification<MerchantStore> searchStores(
            String storeCode,
            String storeName,
            LocalDate storeCreationDate) {

        return (Root<MerchantStore> root, // root reference hai //select * from MerchantStore
                CriteriaQuery<?> query,  // SELECT query hai
                CriteriaBuilder cb) -> { //Query banane ka factory / tool


            Predicate predicate = cb.conjunction(); // WHERE 1=1 //Predicate = WHERE condition

            //conjunction Ye ek default TRUE condition banata hai
            if (storeCode != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("storeCode"), storeCode)
                );
            }

            if (storeName != null) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("storeName")),
                                "%" + storeName.toLowerCase() + "%"
                        )
                );
            }

            if (storeCreationDate != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(
                                cb.function("DATE", LocalDate.class, root.get("createdAt")),
                                storeCreationDate
                        )
                );
            }

            return predicate;
        };
    }
}
