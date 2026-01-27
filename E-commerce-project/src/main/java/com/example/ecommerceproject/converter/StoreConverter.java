package com.example.ecommerceproject.converter;

import com.example.ecommerceproject.Entity.*;
import com.example.ecommerceproject.dto.*;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StoreConverter {
    // Convert User + StoreDto to ReadableMerchantStore
    public ReadableMerchantStore convertToReadable(User user, MerchantStoreDto storeDto) {
        // -------- User --------
        ReadAbleUser readAbleUser = new ReadAbleUser();
        if (user != null) {
            readAbleUser.setId(user.getId());
            readAbleUser.setUserName(user.getUserName());
            readAbleUser.setEmail(user.getEmail());

            List<RoleDto> roles = new ArrayList<>();
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                for (Role role : user.getRoles()) {
                    RoleDto dto = new RoleDto();
                    dto.setId(role.getId());
                    dto.setRoleName(role.getRoleName());
                    roles.add(dto);
                }
            }
            readAbleUser.setRoles(roles);
        }

        // -------- Store --------
        ReadAbleMerchantStore readAbleStore = new ReadAbleMerchantStore();
        if (storeDto != null) {
            readAbleStore.setId(storeDto.getId());
            readAbleStore.setStoreName(storeDto.getStoreName());
            readAbleStore.setDescription(storeDto.getDescription());
            readAbleStore.setLogoUrl(storeDto.getLogoUrl());
            readAbleStore.setOwnerName(storeDto.getOwnerName());
            readAbleStore.setOwnerEmail(storeDto.getOwnerEmail());
            readAbleStore.setPhone(storeDto.getPhone());
            readAbleStore.setCountry(storeDto.getCountry());
            readAbleStore.setCity(storeDto.getCity());
            readAbleStore.setAddress(storeDto.getAddress());

            // Convert addresses
            if (storeDto.getStoreAddresses() != null) {
                readAbleStore.setStoreAddresses(convertAddressesFromDto(storeDto.getStoreAddresses()));
            }
        }

        // -------- Combine --------
        ReadableMerchantStore readable = new ReadableMerchantStore();
        readable.setReadAbleUser(readAbleUser);
        readable.setReadableMerchantStore(readAbleStore);

        return readable;
    }

    // Convert StoreAddress list → ReadAbleStoreAddress list
// Convert list of StoreAddressDto to ReadAbleStoreAddress
    private List<ReadAbleStoreAddress> convertAddressesFromDto(List<StoreAddressDto> dtos) {
        List<ReadAbleStoreAddress> list = new ArrayList<>();
        for (StoreAddressDto dto : dtos) {
            ReadAbleStoreAddress readAbleStoreAddress = new ReadAbleStoreAddress();
            readAbleStoreAddress.setId(dto.getId());
            readAbleStoreAddress.setCountry(dto.getCountry());
            readAbleStoreAddress.setCity(dto.getCity());
            readAbleStoreAddress.setPostalCode(dto.getPostalCode());
            readAbleStoreAddress.setAddress(dto.getAddress());
            list.add(readAbleStoreAddress);
        }
        return list;
    }

    public ReadAbleProduct convertToReadable(Product product) {
        if (product == null) return null;

        ReadAbleProduct dto = new ReadAbleProduct();
        dto.setId(product.getId());
        dto.setProductImage(product.getProductImage());
        dto.setSku(product.getSku());
        dto.setRefSku(product.getRefSku());
        dto.setAvailable(product.isAvailable());
        dto.setActive(product.isActive());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setDateAvailable(product.getDateAvailable());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        // Map IDs only
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setMerchantStoreId(product.getMerchantStore() != null ? product.getMerchantStore().getId() : null);

        // ===== Map Product Descriptions =====
        if (product.getProductDescriptions() != null) {
            List<ReadAbleProductDescription> readableDescriptions = new ArrayList<>();
            for (ProductDescription desc : product.getProductDescriptions()) {
                ReadAbleProductDescription readDto = new ReadAbleProductDescription();
                readDto.setName(desc.getName());
                readDto.setDescription(desc.getDescription());
                readDto.setDescriptionType(desc.getDescriptionType());
                readDto.setLanguage(desc.getLanguage());
                readableDescriptions.add(readDto);

            }
            dto.setProductDescriptions(readableDescriptions);
        }

        return dto;
    }


    public ReadAbleCategory convertToReadable(Category category) {
        if (category == null) return null;

        ReadAbleCategory dto = new ReadAbleCategory();

        // ================= Category basic fields =================
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setCategoryImage(category.getCategoryImage());
        dto.setSortOrder(category.getSortOrder());
        dto.setCategoryStatus(category.getCategoryStatus());
        dto.setVisible(category.getVisible());
        dto.setDepth(category.getDepth());
        dto.setLineage(category.getLineage());
        dto.setFeatured(category.getFeatured());
        dto.setActive(category.getIsActive());


        // ================= CategoryDescriptions =================
        if (category.getCategoryDescriptions() != null && !category.getCategoryDescriptions().isEmpty()) {
            List<ReadAbleCategoryDescription> readableDescriptions = new ArrayList<>();
            for (CategoryDescription desc : category.getCategoryDescriptions()) {
                ReadAbleCategoryDescription rDesc = new ReadAbleCategoryDescription();
                rDesc.setId(desc.getId());
                rDesc.setDescription(desc.getDescription());
                rDesc.setDescriptionType(desc.getDescriptionType());
                rDesc.setCreatedAt(desc.getCreatedAt());
                rDesc.setUpdatedAt(desc.getUpdatedAt());
                readableDescriptions.add(rDesc);
            }
            dto.setCategoryDescriptions(readableDescriptions);
        } else {
            dto.setCategoryDescriptions(new ArrayList<>()); // empty list if no descriptions
        }

        return dto;
    }

}


