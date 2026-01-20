package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.dto.RoleDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface RoleService {

    // create role
    RoleDto createRole(RoleDto roleDto);
    // get All role
    List<RoleDto> getAllRole();

    //get single role
    Role getById(Long id);
    //update role
    RoleDto updateRole(Long id,RoleDto roleDto);

    // delete role
    RoleDto deleteRole(Long id);

    void assignRoleToUser(Long userId, long RoleId);

    // ✅ Assign Permissions to Role (DTO-based)
    RoleDto assignPermissions(Long roleId, RoleDto dto);
}
