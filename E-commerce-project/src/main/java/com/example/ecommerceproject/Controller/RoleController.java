package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.Service.RoleService;
import com.example.ecommerceproject.Service.UserService;
import com.example.ecommerceproject.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    // Create role
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(roleDto);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRole() {
        List<RoleDto> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }

    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable Long id) {
        Role roleDto = roleService.getById(id);
        return ResponseEntity.ok(roleDto);
    }

    // Update role
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id,
                                              @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(updatedRole);
    }

    // Delete role
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted successfully");
    }

    // Assign role to user
    // ✅ Assign Permissions to Role
    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<RoleDto> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody RoleDto dto
    ) {
        RoleDto roleDto = roleService.assignPermissions(roleId, dto);
        return ResponseEntity.ok(roleDto);
    }



}
