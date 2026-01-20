package com.example.ecommerceproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {

    private Long id;                // Optional: for update
    private String userName;
    private String email;

    private Boolean isActive = Boolean.FALSE; // primitive boolean se Boolean me convert karo


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> roleIds = new ArrayList<>(); // Role IDs sent from client

    private List<RoleDto> roles = new ArrayList<>();

    public UserDto() {
    }

    public UserDto(Long id, String userName, String email, List<Long> roleIds, List<RoleDto> roles) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.roleIds = roleIds;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
