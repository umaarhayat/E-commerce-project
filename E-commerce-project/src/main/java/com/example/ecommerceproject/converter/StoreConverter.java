package com.example.ecommerceproject.converter;

import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.Entity.StoreAddress;
import com.example.ecommerceproject.Entity.User;
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

        ReadAbleProduct readAbleProduct = new ReadAbleProduct();
        readAbleProduct.setId(product.getId());
        readAbleProduct.setSku(product.getSku());
        readAbleProduct.setRefSku(product.getRefSku());
        readAbleProduct.setAvailable(product.isAvailable());
        readAbleProduct.setActive(product.isActive());
        readAbleProduct.setPrice(product.getPrice());
        readAbleProduct.setQuantity(product.getQuantity());
        readAbleProduct.setDateAvailable(product.getDateAvailable());
        readAbleProduct.setCreatedAt(product.getCreatedAt());
        readAbleProduct.setUpdatedAt(product.getUpdatedAt());

        // Explicitly set categoryId from either field or category object
        if (product.getCategory() != null) {
            readAbleProduct.setCategoryId(product.getCategory().getId());
        } else {
            readAbleProduct.setCategoryId(product.getCategoryId());
        }

        // Optional: set merchantStoreId if you have merchantStore relation
        if (product.getMerchantStoreId() != null) {
            readAbleProduct.setMerchantStoreId(product.getMerchantStoreId());
        }

        return readAbleProduct;
    }

}
