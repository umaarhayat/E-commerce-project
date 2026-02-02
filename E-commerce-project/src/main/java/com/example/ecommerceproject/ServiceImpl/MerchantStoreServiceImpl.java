package com.example.ecommerceproject.ServiceImpl;
import com.example.ecommerceproject.Entity.MerchantStore;
import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.Entity.StoreAddress;
import com.example.ecommerceproject.Entity.User;
import com.example.ecommerceproject.Exception.MerchantStoreNotFoundException;
import com.example.ecommerceproject.Exception.RoleNotFoundException;
import com.example.ecommerceproject.Exception.UserNotFoundException;
import com.example.ecommerceproject.Repository.MerchantStoreRepo;
import com.example.ecommerceproject.Repository.RoleRepo;
import com.example.ecommerceproject.Repository.UserRepo;
import com.example.ecommerceproject.Service.*;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.*;
import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.dto.ReadAbleUser;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import com.example.ecommerceproject.specification.MerchantStoreSpecification;
import org.springframework.core.io.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MerchantStoreServiceImpl implements MerchantStoreService {

    @Autowired
    private MerchantStoreRepo merchantStoreRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StoreConverter storeConverter;
    @Autowired
    private EmailService emailService;

    @Autowired
    private MerchantStoreSpecification merchantStoreSpecification;


    @Autowired
    private FileStorageService fileStorageService;

    // ================= CREATE =================
    @Override
    public ReadableMerchantStore createMerchantStore(PersistableMerchanStore persistable) {
        // 1️⃣ Validate input
        if (persistable == null || persistable.getUserDto() == null || persistable.getMerchantStoreDto() == null) {
            throw new MerchantStoreNotFoundException("User and Store data are required");
        }
        UserDto inputUserDto = persistable.getUserDto();

        // 2️⃣ Check if user already exists
        User existingUser = userService.findByEmail(inputUserDto.getEmail());
        if (existingUser != null && existingUser.getUserName() != null) {
            throw new UserNotFoundException("User email already exists");
        }
        // 3️⃣ Create new User
        User user = new User();
        user.setUserName(inputUserDto.getUserName());
        user.setEmail(inputUserDto.getEmail());


        // 4️⃣ Assign Roles to User
        List<Role> roles = new ArrayList<>();
        if (inputUserDto.getRoleIds() != null) {
            for (Long roleId : inputUserDto.getRoleIds()) {
                Role role = roleService.getById(roleId);
                if (role == null) {
                    throw new RoleNotFoundException("Role not found with id: " + roleId);
                }
                roles.add(role);
            }
        }
        user.setRoles(roles);

        // 5️⃣ Save User
        user = userRepo.save(user);

        // 6️⃣ Create MerchantStore
        MerchantStore store = modelMapper.map(persistable.getMerchantStoreDto(), MerchantStore.class);
        store.setUser(user);
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());

        // ✅ Generate storeCode if null or empty
        if (store.getStoreCode() == null || store.getStoreCode().isEmpty()) {
            store.setStoreCode("STORE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        // 7️⃣ Save Addresses
        if (persistable.getMerchantStoreDto().getStoreAddresses() != null) {
            List<StoreAddress> addresses = new ArrayList<>();
            for (StoreAddressDto dto : persistable.getMerchantStoreDto().getStoreAddresses()) {
                StoreAddress address = modelMapper.map(dto, StoreAddress.class);
                address.setMerchantStore(store); // link parent
                addresses.add(address);
            }
            store.setAddresses(addresses);
        }

        // 8️⃣ Save MerchantStore
        MerchantStore savedStore = merchantStoreRepo.save(store);
        // 6️⃣ Send Email to User
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(user.getEmail());
        emailDto.setSubject("Store Created Successfully");
        emailDto.setBody(
                "Dear " + user.getUserName() + ",\n\n" +
                        "Your store '" + savedStore.getStoreName() + "' has been created successfully.\n" +
                        "Store Code: " + savedStore.getStoreCode() + "\n\n" +
                        "Thank you for using our platform."
        );
        emailService.sendEmail(emailDto);

        // 9️⃣ Map addresses manually to DTO
        MerchantStoreDto storeDto = modelMapper.map(savedStore, MerchantStoreDto.class);
        if (savedStore.getAddresses() != null) {
            List<StoreAddressDto> addressDtos = new ArrayList<>();
            for (StoreAddress addr : savedStore.getAddresses()) {
                StoreAddressDto dto = modelMapper.map(addr, StoreAddressDto.class);
                addressDtos.add(dto);
            }
            storeDto.setStoreAddresses(addressDtos);
        }

        //  🔟 Convert to ReadableMerchantStore
        return storeConverter.convertToReadable(user, storeDto);
    }
    // ================= GET ALL =================
    @Override
    public List<ReadableMerchantStore> getAllMerchantStore() {
        List<MerchantStore> stores = merchantStoreRepo.findAll();
        if (stores.isEmpty()) throw new MerchantStoreNotFoundException("No merchant stores found");

        List<ReadableMerchantStore> result = new ArrayList<>();
        for (MerchantStore store : stores) {
            // Skip if store is soft-deleted
            if (store.getIsDelete() != null && store.getIsDelete()) {
                continue;
            }
            User user = store.getUser();
            // Skip if user is null or user is soft-deleted (assuming User has isDelete or isActive field)
            if (user == null || (user.getActive() != null && !user.getActive())) {
                continue;
            }
            MerchantStoreDto storeDto = modelMapper.map(store, MerchantStoreDto.class);
            result.add(storeConverter.convertToReadable(user, storeDto));
        }
        if (result.isEmpty()) throw new MerchantStoreNotFoundException("No active merchant stores found");
        return result;
    }
    // ================= GET BY ID =================
    @Override
    public ReadableMerchantStore getById(Long id) {
        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));

        // Safe null check for soft delete
        if (Boolean.TRUE.equals(store.getIsDelete())) {
            throw new MerchantStoreNotFoundException("Store not found with id: " + id);
        }
        User user = store.getUser();
        // Optional: skip if user is inactive/soft-deleted
        if (user == null || Boolean.FALSE.equals(user.getActive())) {
            throw new MerchantStoreNotFoundException("Store not found with id: " + id);
        }

        MerchantStoreDto storeDto = modelMapper.map(store, MerchantStoreDto.class);
        return storeConverter.convertToReadable(user, storeDto);
    }

    // ================= UPDATE =================
    @Override
    public ReadableMerchantStore updateMerchantStore(Long id, ReadableMerchantStore request) {
        if (request == null || request.getReadableMerchantStore() == null || request.getReadAbleUser() == null) {
            throw new MerchantStoreNotFoundException("Both user and store data are required");
        }

        // Fetch store
        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));

        // ===== Update Store fields =====
        ReadAbleMerchantStore storeDto = request.getReadableMerchantStore();
        store.setStoreName(storeDto.getStoreName());
        store.setDescription(storeDto.getDescription());
        store.setLogoUrl(storeDto.getLogoUrl());
        store.setOwnerName(storeDto.getOwnerName());
        store.setOwnerEmail(storeDto.getOwnerEmail());
        store.setPhone(storeDto.getPhone());
        store.setCountry(storeDto.getCountry());
        store.setCity(storeDto.getCity());
        store.setAddress(storeDto.getAddress());

        // ===== Update User fields =====
        ReadAbleUser userDto = request.getReadAbleUser();
        User user = store.getUser();
        if (user != null) {
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());

            // ===== Update Roles =====
            if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
                Set<Role> updatedRoles = new HashSet<>();
                for (RoleDto r : userDto.getRoles()) {
                    Role role = roleRepo.findById(r.getId())
                            .orElseThrow(() -> new RuntimeException("Role not found with id: " + r.getId()));
                    updatedRoles.add(role);
                }
                user.setRoles((List<Role>) updatedRoles); // replace old roles with new ones
            }

            userRepo.save(user);
        }

        // Save store
        merchantStoreRepo.save(store);

        // Return readable response
        return storeConverter.convertToReadable(user, modelMapper.map(store, MerchantStoreDto.class));
    }


    // ================= HARD DELETE =================
    @Override
    public void deleteMerchantStore(Long id) {
        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));

        User user = store.getUser();
        if (user != null) userRepo.delete(user);

        merchantStoreRepo.delete(store);
    }
    // =============== SOFT DELETE ============
    @Override
    public String softDeleteMerchantStore(Long id) {

        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));
        store.getIsDelete();
        merchantStoreRepo.save(store);
        return "MerchantStore  soft deleted successfully";
    }

    //================USER ACTIVATED / USER DEACTIVATED =====================
    @Override
    public String activateUserOfStore(Long storeId, boolean isActive) {
        MerchantStore store = merchantStoreRepo.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Merchant Store not found"));

        if(store.getUser() == null){
            throw new RuntimeException("No user assigned to this store");
        }

        store.getUser().setActive(isActive); // User entity me active field
        merchantStoreRepo.save(store); // Cascade ke through user bhi save ho jayega

        return isActive ? "User activated successfully" : "User deactivated successfully";
    }
// ==================STORE ACTIVATED / STORE DEACTIVATED ==============
    @Override
    public String activateOrDeactivateStore(Long storeId, boolean isActive) {
        MerchantStore store = merchantStoreRepo.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Merchant Store not found"));

        // Soft delete store check (optional)
        if (store.getIsDelete() != null && store.getIsDelete()) {
            throw new RuntimeException("Cannot activate/deactivate a deleted store");
        }
        // Update store active status
        store.setIsActive(isActive);
        store.setUpdatedAt(LocalDateTime.now());
        merchantStoreRepo.save(store);
        return isActive ? "Store activated successfully" : "Store deactivated successfully";
    }

    // ================== GIT MERCHANTSTORE STORE BY STORE CODE =================
    @Override
    public ReadableMerchantStore getMerchantStoreByStoreCode(String storeCode) {
        MerchantStore store = merchantStoreRepo.findByStoreCode(storeCode)
                .orElseThrow(() -> new MerchantStoreNotFoundException(
                        "Merchant store not found with storeCode: " + storeCode
                ));

        if (store.getIsDelete() != null && store.getIsDelete()) {
            throw new MerchantStoreNotFoundException("Merchant store is deleted: " + storeCode);
        }

        User user = store.getUser();
        MerchantStoreDto storeDto = modelMapper.map(store, MerchantStoreDto.class);

        // Convert to ReadableMerchantStore
        return storeConverter.convertToReadable(user, storeDto);
    }
// ================ UPLOADING STORE LOGO =============================
@Override
public String uploadStoreLogo(Long storeId, MultipartFile logo) {
    if (logo == null || logo.isEmpty()) {
        throw new MerchantStoreNotFoundException("Logo file is required");
    }

    MerchantStore store = merchantStoreRepo.findById(storeId)
            .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found"));

    if (Boolean.TRUE.equals(store.getIsDelete())) {
        throw new RuntimeException("Cannot upload logo for deleted store");
    }

    String directory = "stores/" + store.getStoreCode();

    // Delete old logo if exists
    if (store.getLogo() != null && !store.getLogo().isBlank()) {
        fileStorageService.deleteFile(directory, store.getLogo());
    }

    // Save new logo
    String originalFileName = logo.getOriginalFilename();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String newFileName = "logo_" + System.currentTimeMillis() + extension;

    fileStorageService.uploadFile(logo, directory, newFileName);

    // Update DB
    store.setLogo(newFileName);
    store.setUpdatedAt(LocalDateTime.now());
    merchantStoreRepo.save(store);

    return "Store logo uploaded successfully";
}

    // ================= DOWNLOAD LOGO =====================
    @Override
    public Resource downloadStoreLogo(Long storeId) {
        MerchantStore store = merchantStoreRepo.findById(storeId)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found"));

        if (store.getLogo() == null || store.getLogo().isBlank()) {
            throw new RuntimeException("Logo not found for store");
        }
        String directory = "stores/" + store.getStoreCode();
        return fileStorageService.downloadFile(directory, store.getLogo());
    }

    // ================= DELETE LOGO =====================
    @Override
    public String deleteStoreLogo(Long storeId) {
        MerchantStore store = merchantStoreRepo.findById(storeId)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found"));

        if (store.getLogo() == null || store.getLogo().isBlank()) {
            throw new MerchantStoreNotFoundException("No logo found for this store");
        }

        String directory = "stores/" + store.getStoreCode();
        fileStorageService.deleteFile(directory, store.getLogo());

        store.setLogo(null);
        store.setUpdatedAt(LocalDateTime.now());
        merchantStoreRepo.save(store);

        return "Store logo deleted successfully";
    }


//    @Override
//    public List<ReadAbleMerchantStore> getStores(
//            String storeCode,
//            String storeName,
//            LocalDate storeCreationDate) {
//
//        List<MerchantStore> storeList;
//
//        // Fetch all stores or filter by params
//        if (storeCode == null && storeName == null && storeCreationDate == null) {
//            storeList = merchantStoreRepo.findAll();
//        } else {
//            storeList = merchantStoreRepo.searchStores(storeCode, storeName, storeCreationDate);
//        }
//
//        List<ReadAbleMerchantStore> readableStores = new ArrayList<>();
//
//        for (MerchantStore store : storeList) {
//            if (store != null) {
//                ReadAbleMerchantStore dto = new ReadAbleMerchantStore();
//
//                // ---- Direct Fields ----
//                dto.setId(store.getId());
//                dto.setStoreName(store.getStoreName());
//                dto.setStoreCode(store.getStoreCode());
//                dto.setDescription(store.getDescription());
//                dto.setLogo(store.getLogo());
//                dto.setLogoUrl(store.getLogoUrl());
//                dto.setOwnerName(store.getOwnerName());
//                dto.setOwnerEmail(store.getOwnerEmail());
//                dto.setPhone(store.getPhone());
//                dto.setCountry(store.getCountry());
//                dto.setCity(store.getCity());
//                dto.setAddress(store.getAddress());
//                dto.setCreatedAt(store.getCreatedAt());
//                dto.setUpdatedAt(store.getUpdatedAt());
//                dto.setIsDelete(store.getIsDelete());
//                dto.setActive(store.getIsActive());
//
//                // ---- Nested User ----
//                if (store.getUser() != null) {
//                    ReadAbleUser readAbleUser = new ReadAbleUser();
//                    readAbleUser.setId(store.getUser().getId());
//                    readAbleUser.setUserName(store.getUser().getUserName());
//                    readAbleUser.setEmail(store.getUser().getEmail());
//                    dto.setReadAbleUser(readAbleUser);
//                }
//
//                // ---- Nested Addresses ----
//                if (store.getAddresses() != null && !store.getAddresses().isEmpty()) {
//                    List<ReadAbleStoreAddress> addressList = new ArrayList<>();
//                    for (StoreAddress addr : store.getAddresses()) {
//                        ReadAbleStoreAddress addressDto = new ReadAbleStoreAddress();
//                        addressDto.setId(addr.getId());
//                        addressDto.setAddress(addr.getAddress());
//                        addressDto.setCity(addr.getCity());
//                        addressDto.setCountry(addr.getCountry());
//                        addressList.add(addressDto);
//                    }
//                    dto.setStoreAddresses(addressList);
//                }
//
//                readableStores.add(dto);
//            }
//        }
//
//        return readableStores;
//    }

    @Override
    public List<ReadAbleMerchantStore> getStores(
            String storeCode,
            String storeName,
            LocalDate storeCreationDate) {

        List<MerchantStore> storeList;

        // ✅ Use Criteria Specification
        if (storeCode == null && storeName == null && storeCreationDate == null) {
            storeList = merchantStoreRepo.findAll();
        } else {
            Specification<MerchantStore> spec =
                    merchantStoreSpecification.searchStores(
                            storeCode, storeName, storeCreationDate);

            storeList = merchantStoreRepo.findAll(spec);
        }

        // ===== DTO Mapping (UNCHANGED) =====
        List<ReadAbleMerchantStore> readableStores = new ArrayList<>();

        for (MerchantStore store : storeList) {
            if (store != null) {
                ReadAbleMerchantStore dto = new ReadAbleMerchantStore();

                dto.setId(store.getId());
                dto.setStoreName(store.getStoreName());
                dto.setStoreCode(store.getStoreCode());
                dto.setDescription(store.getDescription());
                dto.setLogo(store.getLogo());
                dto.setLogoUrl(store.getLogoUrl());
                dto.setOwnerName(store.getOwnerName());
                dto.setOwnerEmail(store.getOwnerEmail());
                dto.setPhone(store.getPhone());
                dto.setCountry(store.getCountry());
                dto.setCity(store.getCity());
                dto.setAddress(store.getAddress());
                dto.setCreatedAt(store.getCreatedAt());
                dto.setUpdatedAt(store.getUpdatedAt());
                dto.setIsDelete(store.getIsDelete());
                dto.setActive(store.getIsActive());

                if (store.getUser() != null) {
                    ReadAbleUser user = new ReadAbleUser();
                    user.setId(store.getUser().getId());
                    user.setUserName(store.getUser().getUserName());
                    user.setEmail(store.getUser().getEmail());
                    dto.setReadAbleUser(user);
                }

                if (store.getAddresses() != null) {
                    List<ReadAbleStoreAddress> addresses = new ArrayList<>();
                    for (StoreAddress addr : store.getAddresses()) {
                        ReadAbleStoreAddress a = new ReadAbleStoreAddress();
                        a.setId(addr.getId());
                        a.setAddress(addr.getAddress());
                        a.setCity(addr.getCity());
                        a.setCountry(addr.getCountry());
                        addresses.add(a);
                    }
                    dto.setStoreAddresses(addresses);
                }

                readableStores.add(dto);
            }
        }

        return readableStores;
    }

}

