package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Permission;
import com.example.ecommerceproject.dto.PermissionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
        PermissionDto createPermission(PermissionDto dto);
        List<PermissionDto> getAllPermissions();
        PermissionDto getPermissionById(Long id);


}
