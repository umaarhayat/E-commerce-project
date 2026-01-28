package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.MerchantStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantStoreRepo extends JpaRepository<MerchantStore,Long> {
    Optional<MerchantStore> findByStoreCode(String storeCode);



    @Query(
            "SELECT s FROM MerchantStore s " +
                    "WHERE (:storeCode IS NULL OR s.storeCode = :storeCode) " +
                    "AND (:storeName IS NULL OR LOWER(s.storeName) LIKE LOWER(CONCAT('%', :storeName, '%'))) " +
                    "AND (:storeCreationDate IS NULL OR DATE(s.createdAt) = :storeCreationDate)"
    )
    List<MerchantStore> searchStores(
            @Param("storeCode") String storeCode,
            @Param("storeName") String storeName,
            @Param("storeCreationDate") LocalDate storeCreationDate
    );

}
