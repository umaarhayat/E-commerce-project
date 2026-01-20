package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Permission;
import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.Entity.User;
import com.example.ecommerceproject.Exception.RoleNotFoundException;
import com.example.ecommerceproject.Repository.PermissionRepo;
import com.example.ecommerceproject.Repository.RoleRepo;
import com.example.ecommerceproject.Repository.UserRepo;
import com.example.ecommerceproject.Service.RoleService;
import com.example.ecommerceproject.dto.RoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PermissionRepo permissionRepo;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        Role savedRole = roleRepo.save(role);
        return modelMapper.map(savedRole, RoleDto.class);
    }

    @Override
    public List<RoleDto> getAllRole() {
        List<Role> roles = roleRepo.findAll();
        if (roles.isEmpty()) throw new RoleNotFoundException("No roles found in database");
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roles) {
            roleDtos.add(modelMapper.map(role, RoleDto.class));
        }
        return roleDtos;
    }

    // **FIXED**: Return Role entity instead of RoleDto
    @Override
    public Role getById(Long id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        role.setRoleName(roleDto.getRoleName());
        return modelMapper.map(roleRepo.save(role), RoleDto.class);
    }

    @Override
    public RoleDto deleteRole(Long id) {
        Role role = roleRepo.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        roleRepo.delete(role);
        return null;
    }

    @Override
    public void assignRoleToUser(Long userId, long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RoleNotFoundException("User not found with id: " + userId));

        user.getRoles().add(role);
        userRepo.save(user);
    }

    @Override
    public RoleDto assignPermissions(Long roleId, RoleDto dto) {
        // 1️⃣ Fetch Role from DB
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));

        // 2️⃣ Fetch Permissions from DB using IDs
        Set<Permission> permissions = new HashSet<>(permissionRepo.findAllById(dto.getPermissionIds()));

        if (permissions.isEmpty()) {
            throw new RuntimeException("No valid permissions found to assign");
        }

        // ️ Assign Permissions to Role
        role.setPermissions(permissions);

        //  Save Role
        Role savedRole = roleRepo.save(role);

        // ️ Map Role entity to RoleDto for response
        RoleDto responseDto = new RoleDto();
        responseDto.setId(savedRole.getId());
        responseDto.setRoleName(savedRole.getRoleName());

        //  Set permissionIds without Stream API
        Set<Long> permissionIds = new HashSet<>();
        for (Permission p : savedRole.getPermissions()) {
            permissionIds.add(p.getId());
        }
        responseDto.setPermissionIds(permissionIds);

        return responseDto;
    }


}
