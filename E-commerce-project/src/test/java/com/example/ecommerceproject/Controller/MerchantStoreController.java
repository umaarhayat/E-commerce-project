package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Service.MerchantStoreService;
import com.example.ecommerceproject.Service.UserService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.PageResponse;
import com.example.ecommerceproject.dto.ReadAbleMerchantStore;
import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Merchant Store API", description = "Endpoints for managing merchant stores")
@RequestMapping("api/merchantStore")
public class MerchantStoreController {

    @Autowired
    private MerchantStoreService merchantStoreService;

    @Autowired
    private UserService userService;

    // ================= CREATE =================
    @PostMapping
    @Operation(summary = "Create a new merchant store",
            description = "Creates a new merchant store and sends email notification")
    public GenericResponse createMerchantStore(@RequestBody PersistableMerchanStore persistable) {
        ReadableMerchantStore store = merchantStoreService.createMerchantStore(persistable);
        return GenericResponse.success(store, "Store created successfully and email sent");
    }
    // ================= GET ALL =================
//    /**
//     * Get paginated list of active merchant stores
//     *
//     * @param page page number (default 1)
//     * @param size page size (default 10)
//     * @return paginated list of ReadableMerchantStore
//     */
////    @GetMapping
//    public GenericResponse<PageResponse<ReadableMerchantStore>> getAllStores(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        PageResponse<ReadableMerchantStore> storesPage =
//                merchantStoreService.getAllMerchantStore(page, size);
//
//        return GenericResponse.success(storesPage, "Stores fetched successfully");
//    }


    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Get merchant store by ID", description = "Fetch a merchant store using its ID")
    public GenericResponse<ReadableMerchantStore> getStoreById(@PathVariable Long id) {
        ReadableMerchantStore store = merchantStoreService.getById(id);
        return GenericResponse.success(store, "Merchant Store retrieved successfully");
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(summary = "Update merchant store", description = "Update an existing merchant store by ID")
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
    @Operation(summary = "Soft delete merchant store", description = "Soft deletes a merchant store by ID")
    public GenericResponse<String> softDeleteMerchantStore(@PathVariable Long id) {
        String message = merchantStoreService.softDeleteMerchantStore(id);
        return GenericResponse.success(message, "Merchant Store soft-deleted successfully");
    }

    @PatchMapping("/{storeId}/user/status")
    @Operation(summary = "Activate/Deactivate store user", description = "Update active status of the user associated with the store")
    public GenericResponse<String> activateUserOfStore(
            @PathVariable Long storeId,
            @RequestParam boolean isActive) {
        String message = merchantStoreService.activateUserOfStore(storeId, isActive);
        return GenericResponse.success(message, "Store user status updated successfully");
    }

    // ================= ACTIVATE / DEACTIVATE STORE =================
    @PatchMapping("/stores/{id}/status")
    @Operation(summary = "Activate/Deactivate store", description = "Update active status of the merchant store")
    public GenericResponse<String> updateStoreStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        String message = merchantStoreService.activateOrDeactivateStore(id, isActive);
        return GenericResponse.success(message, "Store status updated successfully");
    }

    // ================= GET STORE BY STORE CODE =================
    @GetMapping("/code/{storeCode}")
    @Operation(summary = "Get store by store code", description = "Fetch a merchant store by its unique store code")
    public GenericResponse<ReadableMerchantStore> getStoreByStoreCode(@PathVariable String storeCode) {
        ReadableMerchantStore store = merchantStoreService.getMerchantStoreByStoreCode(storeCode);
        return GenericResponse.success(store, "Merchant Store retrieved successfully by store code");
    }

    // ================= POST UPLOADING LOGO =================
    @PostMapping(value = "/{storeId}/upload-logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload store logo", description = "Upload a logo image file for the merchant store")
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
    @Operation(summary = "Download store logo", description = "Download the logo image of the merchant store")
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
    @Operation(summary = "Delete store logo", description = "Delete the logo image of the merchant store")
    public GenericResponse deleteStoreLogo(@PathVariable Long storeId) {
        String deletedLogoName = merchantStoreService.deleteStoreLogo(storeId);

        if (deletedLogoName == null) {
            return GenericResponse.error("No logo found for this store", "LOGO_NOT_FOUND");
        }

        return GenericResponse.success(deletedLogoName, "Logo deleted successfully");
    }

// ========== optional get merchantStore storeName AND storeCode and storeCreationDate
@GetMapping
@Operation(summary = "Get stores with optional filters",
        description = "Fetch paginated list of merchant stores filtered by store code, store name, or creation date")
public GenericResponse<PageResponse<ReadAbleMerchantStore>> getStores(
        @RequestParam(required = false) String storeCode,
        @RequestParam(required = false) String storeName,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate storeCreationDate,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
) {
    PageResponse<ReadAbleMerchantStore> response = merchantStoreService
            .getStores(storeCode, storeName, storeCreationDate, page, size);

    return GenericResponse.success(response, "Stores fetched successfully");
}


}
