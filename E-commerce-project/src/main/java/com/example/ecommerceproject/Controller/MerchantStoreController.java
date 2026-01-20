package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Service.MerchantStoreService;
import com.example.ecommerceproject.Service.UserService;
import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/merchantStore")
public class MerchantStoreController {

    @Autowired
    private MerchantStoreService merchantStoreService;

    @Autowired
    private UserService userService;
    // ================= CREATE =================
    @PostMapping()
    public ResponseEntity<ReadableMerchantStore> createMerchantStore(
            @RequestBody PersistableMerchanStore persistable) {
        ReadableMerchantStore store = merchantStoreService.createMerchantStore(persistable);
        return new ResponseEntity<>(store, HttpStatus.CREATED);
    }

    // ================= GET ALL =================
    @GetMapping()
    public ResponseEntity<List<ReadableMerchantStore>> getAllStores() {
        List<ReadableMerchantStore> stores = merchantStoreService.getAllMerchantStore();
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ReadableMerchantStore> getStoreById(@PathVariable Long id) {
        ReadableMerchantStore store = merchantStoreService.getById(id);
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ReadableMerchantStore> updateStore(
            @PathVariable Long id,
            @RequestBody ReadableMerchantStore request) {
        ReadableMerchantStore updatedStore = merchantStoreService.updateMerchantStore(id, request);
        return new ResponseEntity<>(updatedStore, HttpStatus.OK);
    }

    // =================HARD DELETE =================
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteStore(@PathVariable Long id) {
//        merchantStoreService.deleteMerchantStore(id);
//        return new ResponseEntity<>("Merchant Store deleted successfully", HttpStatus.OK);
//    }

    //=============== SOFT DELETE ===============

    @PutMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteMerchantStore(@PathVariable Long id) {
        String message = merchantStoreService.softDeleteMerchantStore(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PatchMapping("/{storeId}/user/status")
    public ResponseEntity<String> activateUserOfStore(
            @PathVariable Long storeId,
            @RequestParam boolean isActive) {

        String message = merchantStoreService.activateUserOfStore(storeId, isActive);
        return ResponseEntity.ok(message);
    }
//
//
    // Activate / Deactivate store
@PatchMapping("/stores/{id}/status")
public ResponseEntity<String> updateStoreStatus(
        @PathVariable Long id,
        @RequestParam boolean isActive) {

    String message = merchantStoreService.activateOrDeactivateStore(id, isActive);
    return ResponseEntity.ok(message);
}

    // ================= GET STORE BY STORE CODE =================
    @GetMapping("/code/{storeCode}")
    public ResponseEntity<ReadableMerchantStore> getStoreByStoreCode(@PathVariable String storeCode) {
        ReadableMerchantStore store = merchantStoreService.getMerchantStoreByStoreCode(storeCode);
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

}
