package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MerchantStoreService {

    // Create MerchantStore and optionally a new User
    ReadableMerchantStore createMerchantStore(PersistableMerchanStore persistableMerchantStore);

    // Get all MerchantStores
    List<ReadableMerchantStore> getAllMerchantStore();

    // Get a single MerchantStore by ID
    ReadableMerchantStore getById(Long id);

    // Update MerchantStore by ID
    ReadableMerchantStore updateMerchantStore(Long id, ReadableMerchantStore request);

    // Delete MerchantStore by ID
    void deleteMerchantStore(Long id);
    // soft delete merchantStore by id
    String softDeleteMerchantStore(Long id);
    String activateUserOfStore(Long storeId, boolean isActive);
    String activateOrDeactivateStore(Long storeId, boolean isActive);
    ReadableMerchantStore getMerchantStoreByStoreCode(String storeCode);
    String uploadStoreLogo(Long storeId, MultipartFile logo);
    Resource downloadStoreLogo(Long storeId);
    String deleteStoreLogo(Long storeId);
}
