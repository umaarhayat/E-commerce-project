package com.example.ecommerceproject.specification;

import com.example.ecommerceproject.Entity.MerchantStore;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;


public interface MerchantStoreSpecification {

     Specification<MerchantStore> searchStores(String storeCode,
                                               String storeName,
                                               LocalDate storeCreationDate);
}
