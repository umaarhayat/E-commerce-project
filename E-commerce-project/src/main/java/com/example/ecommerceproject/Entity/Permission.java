package com.example.ecommerceproject.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
    @Data
    @Table(name = "permissions")
    public class Permission {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name; // Example: "ADMIN", "USER", "MANAGER", etc.


        @ManyToMany(mappedBy = "permissions")
        private Set<Role> roles; // Roles that have this permission


    public Permission() {
    }

    public Permission(Long id, String name, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}




