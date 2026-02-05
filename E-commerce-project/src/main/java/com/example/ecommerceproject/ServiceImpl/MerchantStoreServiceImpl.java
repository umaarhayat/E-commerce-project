package com.example.ecommerceproject.ServiceImpl;
import com.example.ecommerceproject.Entity.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
//    @Override
//    public PageResponse<ReadableMerchantStore> getAllMerchantStore(
//            int page,
//            int size
//    ) {
//        // Pageable object
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
//
//        // Specification for filtering soft-deleted stores and inactive users
//        Specification<MerchantStore> spec = (root, query, cb) -> {
//            // store.isDelete = false OR null
//            var storePredicate = cb.or(
//                    cb.isNull(root.get("isDelete")),
//                    cb.isFalse(root.get("isDelete"))
//            );
//
//            // join with user
//            var userJoin = root.join("user"); // assumes "user" field in MerchantStore
//            var userPredicate = cb.or(
//                    cb.isNull(userJoin.get("active")),
//                    cb.isTrue(userJoin.get("active"))
//            );
//
//            return cb.and(storePredicate, userPredicate);
//        };
//
//        // Fetch paginated and filtered stores
//        Page<MerchantStore> storePage = merchantStoreRepo.findAll(spec, pageable);
//
//        if (storePage.isEmpty()) {
//            throw new MerchantStoreNotFoundException("No active merchant stores found");
//        }
//
//        // Convert entities to DTOs
//        List<ReadableMerchantStore> dtoList = new ArrayList<>();
//        for (MerchantStore store : storePage.getContent()) {
//            User user = store.getUser();
//            MerchantStoreDto storeDto = modelMapper.map(store, MerchantStoreDto.class);
//            dtoList.add(storeConverter.convertToReadable(user, storeDto));
//        }
//
//        // Build PageResponse
//        PageResponse<ReadableMerchantStore> response = new PageResponse<>();
//        response.setContent(dtoList);
//        response.setPage(storePage.getNumber() + 1);
//        response.setSize(storePage.getSize());
//        response.setTotalPages(storePage.getTotalPages());
//        response.setTotalElements(storePage.getTotalElements());
//
//        return response;
//    }

    // ================= GET BY ID =================
    @Override
    public ReadableMerchantStore getById(Long id) {
        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));

        if (Boolean.TRUE.equals(store.getIsDelete())) {
            throw new MerchantStoreNotFoundException("Store not found with id: " + id + " (store is deleted)");
        }

        User user = store.getUser();
        MerchantStoreDto storeDto = modelMapper.map(store, MerchantStoreDto.class);

        // Agar user missing/inactive hai, null pass kar sakte ho
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
    public PageResponse<ReadAbleMerchantStore> getStores(
            String storeCode,
            String storeName,
            LocalDate storeCreationDate,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        Page<MerchantStore> storePage;

        if (storeCode == null && storeName == null && storeCreationDate == null) {
            storePage = merchantStoreRepo.findAll(pageable);
        } else {
            Specification<MerchantStore> spec =
                    merchantStoreSpecification.searchStores(storeCode, storeName, storeCreationDate);
            storePage = merchantStoreRepo.findAll(spec, pageable);
        }

        List<ReadAbleMerchantStore> dtoList = storePage.getContent().stream().map(store -> {

            ReadAbleMerchantStore dto = new ReadAbleMerchantStore();

            // -------- Store Fields --------
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

            // -------- User Mapping (FIXED) --------
            if (store.getUser() != null) {
                User u = store.getUser();

                ReadAbleUser readUser = new ReadAbleUser();
                readUser.setId(u.getId());
                readUser.setUserName(u.getUserName());
                readUser.setEmail(u.getEmail());
                readUser.setActive(u.getActive());

                // ---- ROLES (MAIN FIX) ----
                List<RoleDto> roleDtos = new ArrayList<>();

                if (u.getRoles() != null && !u.getRoles().isEmpty()) {

                    for (Role role : u.getRoles()) {

                        RoleDto r = new RoleDto();
                        r.setId(role.getId());
                        r.setRoleName(role.getRoleName());

                        // ---- PERMISSIONS ----
                        Set<Long> permissionIds = new HashSet<>();
                        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                            for (Permission p : role.getPermissions()) {
                                permissionIds.add(p.getId());
                            }
                        }

                        r.setPermissionIds(permissionIds);
                        roleDtos.add(r);
                    }
                }

                readUser.setRoles(roleDtos);
                dto.setReadAbleUser(readUser);
            }

            // -------- Store Addresses --------
            if (store.getAddresses() != null && !store.getAddresses().isEmpty()) {
                List<ReadAbleStoreAddress> addresses = new ArrayList<>();
                for (StoreAddress addr : store.getAddresses()) {
                    ReadAbleStoreAddress a = new ReadAbleStoreAddress();
                    a.setId(addr.getId());
                    a.setAddress(addr.getAddress());
                    a.setCity(addr.getCity());
                    a.setCountry(addr.getCountry());
                    a.setPostalCode(addr.getPostalCode());
                    addresses.add(a);
                }
                dto.setStoreAddresses(addresses);
            }

            return dto;

        }).toList();

        // -------- Page Response --------
        PageResponse<ReadAbleMerchantStore> response = new PageResponse<>();
        response.setContent(dtoList);
        response.setPage(storePage.getNumber() + 1);
        response.setSize(storePage.getSize());
        response.setTotalPages(storePage.getTotalPages());
        response.setTotalElements(storePage.getTotalElements());

        return response;
    }

}

