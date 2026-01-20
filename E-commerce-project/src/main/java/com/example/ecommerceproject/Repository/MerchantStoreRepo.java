package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.MerchantStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public interface MerchantStoreRepo extends JpaRepository<MerchantStore,Long> {
    Optional<MerchantStore> findByStoreCode(String storeCode);
}
