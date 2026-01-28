package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Service.MerchantStoreService;
import com.example.ecommerceproject.Service.UserService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.ReadAbleMerchantStore;
import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/merchantStore")
public class MerchantStoreController {

    @Autowired
    private MerchantStoreService merchantStoreService;

    @Autowired
    private UserService userService;

    // ================= CREATE =================
    @PostMapping
    public GenericResponse createMerchantStore(@RequestBody PersistableMerchanStore persistable) {
        ReadableMerchantStore store = merchantStoreService.createMerchantStore(persistable);
        return GenericResponse.success(store, "Store created successfully and email sent");
    }
    // ================= GET ALL =================
    @GetMapping("/all")
    public GenericResponse<List<ReadableMerchantStore>> getAllStores() {
        List<ReadableMerchantStore> stores = merchantStoreService.getAllMerchantStore();
        return GenericResponse.success(stores, "All Merchant Stores retrieved successfully");
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public GenericResponse<ReadableMerchantStore> getStoreById(@PathVariable Long id) {
        ReadableMerchantStore store = merchantStoreService.getById(id);
        return GenericResponse.success(store, "Merchant Store retrieved successfully");
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public GenericResponse<ReadableMerchantStore> updateStore(
            @PathVariable Long id,
            @RequestBody ReadableMerchantStore request) {
        ReadableMerchantStore updatedStore = merchantStoreService.updateMerchantStore(id, request);
        return GenericResponse.success(updatedStore, "Merchant Store updated successfully");
    }

    /*
     * Soft deletes a Merchant Store
     * @param id the ID of the merchant store to soft delete
     * @return success message after soft deletion
     */
    @PutMapping("/soft-delete/{id}")
    public GenericResponse<String> softDeleteMerchantStore(@PathVariable Long id) {
        String message = merchantStoreService.softDeleteMerchantStore(id);
        return GenericResponse.success(message, "Merchant Store soft-deleted successfully");
    }

    @PatchMapping("/{storeId}/user/status")
    public GenericResponse<String> activateUserOfStore(
            @PathVariable Long storeId,
            @RequestParam boolean isActive) {
        String message = merchantStoreService.activateUserOfStore(storeId, isActive);
        return GenericResponse.success(message, "Store user status updated successfully");
    }

    // ================= ACTIVATE / DEACTIVATE STORE =================
    @PatchMapping("/stores/{id}/status")
    public GenericResponse<String> updateStoreStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        String message = merchantStoreService.activateOrDeactivateStore(id, isActive);
        return GenericResponse.success(message, "Store status updated successfully");
    }

    // ================= GET STORE BY STORE CODE =================
    @GetMapping("/code/{storeCode}")
    public GenericResponse<ReadableMerchantStore> getStoreByStoreCode(@PathVariable String storeCode) {
        ReadableMerchantStore store = merchantStoreService.getMerchantStoreByStoreCode(storeCode);
        return GenericResponse.success(store, "Merchant Store retrieved successfully by store code");
    }

    // ================= POST UPLOADING LOGO =================
    @PostMapping(value = "/{storeId}/upload-logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GenericResponse uploadLogo(
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            @PathVariable long storeId
    ) {
        if (logo == null || logo.isEmpty()) {
            return GenericResponse.error("Logo file is required", "FILE_REQUIRED");
        }

        // Upload image and get stored filename
        String uploadedFileName = merchantStoreService.uploadStoreLogo(storeId, logo);

        return GenericResponse.success(uploadedFileName, "Logo uploaded successfully");
    }



    @GetMapping("/{storeId}/download-logo")
    public ResponseEntity<?> downloadStoreLogo(@PathVariable Long storeId) {
        Resource resource = merchantStoreService.downloadStoreLogo(storeId);

        if (resource == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Logo file is required for this store.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // ================= DELETE STORE LOGO =================
    @DeleteMapping("/{storeId}/delete-logo")
    public GenericResponse deleteStoreLogo(@PathVariable Long storeId) {
        String deletedLogoName = merchantStoreService.deleteStoreLogo(storeId);

        if (deletedLogoName == null) {
            return GenericResponse.error("No logo found for this store", "LOGO_NOT_FOUND");
        }

        return GenericResponse.success(deletedLogoName, "Logo deleted successfully");
    }

// ========== optional get merchantStore storeName AND storeCode and storeCreationDate
    @GetMapping
    public GenericResponse<List<ReadAbleMerchantStore>> getStores(
            @RequestParam(required = false) String storeCode,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) LocalDate storeCreationDate) {

        List<ReadAbleMerchantStore> stores =
                merchantStoreService.getStores(storeCode, storeName, storeCreationDate);

        return GenericResponse.success(
                stores,
                "Stores fetched successfully"
        );
    }


}
