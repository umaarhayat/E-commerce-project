package com.example.ecommerceproject.ServiceImpl;
import com.example.ecommerceproject.Entity.MerchantStore;
import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.Entity.StoreAddress;
import com.example.ecommerceproject.Entity.User;
import com.example.ecommerceproject.Exception.MerchantStoreNotFoundException;
import com.example.ecommerceproject.Exception.RoleNotFoundException;
import com.example.ecommerceproject.Exception.UserNotFoundException;
import com.example.ecommerceproject.Repository.MerchantStoreRepo;
import com.example.ecommerceproject.Repository.UserRepo;
import com.example.ecommerceproject.Service.EmailService;
import com.example.ecommerceproject.Service.MerchantStoreService;
import com.example.ecommerceproject.Service.RoleService;
import com.example.ecommerceproject.Service.UserService;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.*;
import com.example.ecommerceproject.persistable.PersistableMerchanStore;
import com.example.ecommerceproject.dto.ReadAbleUser;
import com.example.ecommerceproject.readable.ReadableMerchantStore;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MerchantStoreServiceImpl implements MerchantStoreService {

    @Autowired
    private MerchantStoreRepo merchantStoreRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StoreConverter storeConverter;

    @Autowired
    private EmailService emailService;

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
        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));

        // Update Store
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

        // Update User
        ReadAbleUser userDto = request.getReadAbleUser();
        User user = store.getUser();
        if (user != null) {
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            userRepo.save(user);
        }
        merchantStoreRepo.save(store);

        return storeConverter.convertToReadable(user, modelMapper.map(store, MerchantStoreDto.class));
    }

    // ================= DELETE =================
    @Override
    public void deleteMerchantStore(Long id) {
        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));

        User user = store.getUser();
        if (user != null) userRepo.delete(user);

        merchantStoreRepo.delete(store);
    }
    @Override
    public String softDeleteMerchantStore(Long id) {

        MerchantStore store = merchantStoreRepo.findById(id)
                .orElseThrow(() -> new MerchantStoreNotFoundException("Store not found with id: " + id));
        store.getIsDelete();
        merchantStoreRepo.save(store);
        return "MerchantStore  soft deleted successfully";
    }
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

}
