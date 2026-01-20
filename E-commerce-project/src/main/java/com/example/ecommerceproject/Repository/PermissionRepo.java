package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission,Long> {
}
