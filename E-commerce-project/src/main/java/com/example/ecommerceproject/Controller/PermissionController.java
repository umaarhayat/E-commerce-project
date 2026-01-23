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
            if (createdPermission == null) {
                // Agar DB me data null hai
                return GenericResponse.error("Data not found in database", "DATA_NOT_FOUND");
            }
            return GenericResponse.success(createdPermission, "Permission created successfully");
        }
        // ✅ Get All Permissions
        @GetMapping
        public ResponseEntity<GenericResponse<List<PermissionDto>>> getAllPermissions() {
            List<PermissionDto> permissions = permissionService.getAllPermissions();

            if (permissions == null || permissions.isEmpty()) {
                return ResponseEntity.ok(
                        GenericResponse.error("No permissions found in database", "DATA_NOT_FOUND")
                );
            }
            return ResponseEntity.ok(
                    GenericResponse.success(permissions, "Permissions retrieved successfully")
            );
        }


        // ✅ Get Permission By ID
        @GetMapping("/{id}")
        public ResponseEntity<GenericResponse<PermissionDto>> getPermissionById(@PathVariable Long id) {
            PermissionDto dto = permissionService.getPermissionById(id);

            if (dto == null) {
                // Agar DB me id ke liye data nahi hai
                return ResponseEntity.ok(
                        GenericResponse.error("Data not found in database", "DATA_NOT_FOUND")
                );
            }
            return ResponseEntity.ok(
                    GenericResponse.success(dto, "Permission retrieved successfully")
            );
        }

    }


