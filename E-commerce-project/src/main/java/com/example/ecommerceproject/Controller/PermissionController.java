package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Service.PermissionService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.PermissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // ✅ Create Permission
    @PostMapping
    public GenericResponse createPermission(@RequestBody PermissionDto dto) {
        PermissionDto createdPermission = permissionService.createPermission(dto);

        if (createdPermission == null) {
            return GenericResponse.error(
                    "Data not found in database",
                    "DATA_NOT_FOUND"
            );
        }
        return GenericResponse.success(
                createdPermission,
                "Permission created successfully"
        );
    }

    // ✅ Get All Permissions
    @GetMapping
    public GenericResponse getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();

        if (permissions == null || permissions.isEmpty()) {
            return GenericResponse.error(
                    "No permissions found in database",
                    "DATA_NOT_FOUND"
            );
        }
        return GenericResponse.success(
                permissions,
                "Permissions retrieved successfully"
        );
    }

    // ✅ Get Permission By ID
    @GetMapping("/{id}")
    public GenericResponse getPermissionById(@PathVariable Long id) {
        PermissionDto dto = permissionService.getPermissionById(id);

        if (dto == null) {
            return GenericResponse.error(
                    "Data not found in database",
                    "DATA_NOT_FOUND"
            );
        }
        return GenericResponse.success(
                dto,
                "Permission retrieved successfully"
        );
    }
}
