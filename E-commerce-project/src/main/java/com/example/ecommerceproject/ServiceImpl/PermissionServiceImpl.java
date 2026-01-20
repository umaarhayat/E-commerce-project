package com.example.ecommerceproject.ServiceImpl;
import com.example.ecommerceproject.Entity.Permission;
import com.example.ecommerceproject.Exception.PermissionNotFoundException;
import com.example.ecommerceproject.Repository.PermissionRepo;
import com.example.ecommerceproject.Service.PermissionService;
import com.example.ecommerceproject.dto.PermissionDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

    @Service
    public class PermissionServiceImpl implements PermissionService {

        @Autowired
        private PermissionRepo permissionRepo;
        @Autowired
        private ModelMapper modelMapper;

        @Override
        public PermissionDto createPermission(PermissionDto dto) {
            Permission permission = modelMapper.map(dto, Permission.class);
            Permission savedPermission = permissionRepo.save(permission);
            return modelMapper.map(savedPermission, PermissionDto.class);
        }

        @Override
        public List<PermissionDto> getAllPermissions() {
            List<Permission> permissions = permissionRepo.findAll();
            List<PermissionDto> permissionDtos = new ArrayList<>();

            if (permissions.isEmpty()){


                throw  new PermissionNotFoundException("permission not found with database...");
            }
            for (Permission p : permissions) {
                PermissionDto dto = modelMapper.map(p, PermissionDto.class);
                permissionDtos.add(dto);
            }

            return permissionDtos;
        }


        @Override
        public PermissionDto getPermissionById(Long id) {
            Permission permission = permissionRepo.findById(id)
                    .orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + id));
            return modelMapper.map(permission, PermissionDto.class);
        }
    }



