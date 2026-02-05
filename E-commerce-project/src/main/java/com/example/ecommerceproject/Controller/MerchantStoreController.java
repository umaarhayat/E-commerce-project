package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Service.MerchantStoreService;
import com.example.ecommerceproject.dto.*;
import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/merchantstore")
@Tag(name = "Merchant Store APIs", description = "Merchant Store management APIs")
public class MerchantStoreController {

    @Autowired
    private MerchantStoreService merchantStoreService;

    // ================= CREATE STORE =================
    @PostMapping
    @Operation(
            summary = "Create Merchant Store",
            description = "Creates a merchant store along with user, roles and addresses"
    )
    public GenericResponse<ReadableMerchantStore> createMerchantStore(
            @RequestBody PersistableMerchanStore persistable
    ) {
        return GenericResponse.success(
                merchantStoreService.createMerchantStore(persistable),
                "Merchant store created successfully"
        );
    }

    // ================= GET STORE BY ID =================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get Merchant Store by ID",
            description = "Fetch merchant store details by store ID"
    )
    public GenericResponse<ReadableMerchantStore> getStoreById(@PathVariable Long id) {
        return GenericResponse.success(
                merchantStoreService.getById(id),
                "Merchant store fetched successfully"
        );
    }

    // ================= UPDATE STORE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update Merchant Store",
            description = "Updates merchant store and its associated user & roles"
    )
    public GenericResponse<ReadableMerchantStore> updateStore(
            @PathVariable Long id,
            @RequestBody ReadableMerchantStore request
    ) {
        return GenericResponse.success(
                merchantStoreService.updateMerchantStore(id, request),
                "Merchant store updated successfully"
        );
    }

    // ================= HARD DELETE =================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Merchant Store",
            description = "Permanently deletes merchant store and its user"
    )
    public GenericResponse<String> deleteStore(@PathVariable Long id) {
        merchantStoreService.deleteMerchantStore(id);
        return GenericResponse.success(null, "Merchant store deleted successfully");
    }

    // ================= SOFT DELETE =================
    @PutMapping("/{id}/soft-delete")
    @Operation(
            summary = "Soft Delete Merchant Store",
            description = "Marks merchant store as deleted (soft delete)"
    )
    public GenericResponse<String> softDeleteStore(@PathVariable Long id) {
        return GenericResponse.success(
                merchantStoreService.softDeleteMerchantStore(id),
                "Merchant store soft deleted successfully"
        );
    }

    // ================= ACTIVATE / DEACTIVATE USER =================
    @PatchMapping("/{storeId}/user/status")
    @Operation(
            summary = "Activate or Deactivate Store User",
            description = "Enable or disable the user associated with merchant store"
    )
    public GenericResponse<String> updateUserStatus(
            @PathVariable Long storeId,
            @RequestParam boolean isActive
    ) {
        return GenericResponse.success(
                merchantStoreService.activateUserOfStore(storeId, isActive),
                "User status updated successfully"
        );
    }

    // ================= ACTIVATE / DEACTIVATE STORE =================
    @PatchMapping("/{storeId}/status")
    @Operation(
            summary = "Activate or Deactivate Merchant Store",
            description = "Enable or disable merchant store"
    )
    public GenericResponse<String> updateStoreStatus(
            @PathVariable Long storeId,
            @RequestParam boolean isActive
    ) {
        return GenericResponse.success(
                merchantStoreService.activateOrDeactivateStore(storeId, isActive),
                "Store status updated successfully"
        );
    }

    // ================= GET STORE BY STORE CODE =================
    @GetMapping("/code/{storeCode}")
    @Operation(
            summary = "Get Merchant Store by Store Code",
            description = "Fetch merchant store using unique store code"
    )
    public GenericResponse<ReadableMerchantStore> getByStoreCode(
            @PathVariable String storeCode
    ) {
        return GenericResponse.success(
                merchantStoreService.getMerchantStoreByStoreCode(storeCode),
                "Merchant store fetched successfully"
        );
    }

    // ================= UPLOAD STORE LOGO =================
    @PostMapping(
            value = "/{storeId}/upload-logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Upload Store Logo",
            description = "Uploads or replaces merchant store logo"
    )
    public GenericResponse<String> uploadLogo(
            @PathVariable Long storeId,
            @RequestParam("logo") MultipartFile logo
    ) {
        return GenericResponse.success(
                merchantStoreService.uploadStoreLogo(storeId, logo),
                "Store logo uploaded successfully"
        );
    }

    // ================= DOWNLOAD STORE LOGO =================
    @GetMapping("/{storeId}/download-logo")
    @Operation(
            summary = "Download Store Logo",
            description = "Downloads merchant store logo"
    )
    public ResponseEntity<Resource> downloadLogo(@PathVariable Long storeId) {
        Resource resource = merchantStoreService.downloadStoreLogo(storeId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    // ================= DELETE STORE LOGO =================
    @DeleteMapping("/{storeId}/delete-logo")
    @Operation(
            summary = "Delete Store Logo",
            description = "Deletes merchant store logo"
    )
    public GenericResponse<String> deleteLogo(@PathVariable Long storeId) {
        return GenericResponse.success(
                merchantStoreService.deleteStoreLogo(storeId),
                "Store logo deleted successfully"
        );
    }

    // ================= SEARCH + PAGINATION =================
    @GetMapping
    @Operation(
            summary = "Get Merchant Stores with Filters",
            description = "Fetch merchant stores with optional filters and pagination"
    )
    public GenericResponse<PageResponse<ReadAbleMerchantStore>> getStores(
            @RequestParam(required = false) String storeCode,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate storeCreationDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return GenericResponse.success(
                merchantStoreService.getStores(
                        storeCode,
                        storeName,
                        storeCreationDate,
                        page,
                        size
                ),
                "Merchant stores fetched successfully"
        );
    }
}
