package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.PermissionDto;
import com.example.ecommerceproject.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("api/permissions")  // Base URL
    public class PermissionController {

        @Autowired
        private PermissionService permissionService;

        // ✅ Create Permission

        @PostMapping
        public GenericResponse<PermissionDto> createPermission(@RequestBody PermissionDto dto) {
            PermissionDto createdPermission = permissionService.createPermission(dto);
            return GenericResponse.success(createdPermission, "Permission created successfully");
        }


        // ✅ Get All Permissions
        @GetMapping
        public ResponseEntity<List<PermissionDto>> getAllPermissions() {
            List<PermissionDto> permissions = permissionService.getAllPermissions();
            return ResponseEntity.ok(permissions);
        }

        // ✅ Get Permission By ID
        @GetMapping("/{id}")
        public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
            PermissionDto dto = permissionService.getPermissionById(id);
            return ResponseEntity.ok(dto);
        }
    }


