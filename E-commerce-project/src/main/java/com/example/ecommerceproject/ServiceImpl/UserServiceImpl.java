package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Role;
import com.example.ecommerceproject.Entity.User;
import com.example.ecommerceproject.Exception.RoleNotFoundException;
import com.example.ecommerceproject.Exception.UserNotFoundException;
import com.example.ecommerceproject.Repository.RoleRepo;
import com.example.ecommerceproject.Repository.UserRepo;
import com.example.ecommerceproject.Service.UserService;
import com.example.ecommerceproject.dto.RoleDto;
import com.example.ecommerceproject.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User savedUser = persistUser(userDto);
        return readUser(savedUser);
    }

    private User persistUser(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());

        List<Role> roles = new ArrayList<>();
        if (userDto.getRoleIds() != null) {
            for (Long roleId : userDto.getRoleIds()) {
                Role role = roleRepo.findById(roleId)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
                roles.add(role);
            }
        }
        user.setRoles(roles);
        return userRepo.save(user);
    }

    private UserDto readUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());



        List<RoleDto> roles = new ArrayList<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                RoleDto roleDto = new RoleDto();
                roleDto.setId(role.getId());
                roleDto.setRoleName(role.getRoleName());
                roles.add(roleDto);
            }
        }
        dto.setRoles(roles);
        return dto;
    }

    @Override
    public UserDto updateByUser(Long id, UserDto userDto) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        existingUser.setUserName(userDto.getUserName());
        existingUser.setEmail(userDto.getEmail());
        return readUser(userRepo.save(existingUser));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(readUser(user));
        }
        return userDtos;
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return readUser(user);
    }



    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public UserDto deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        UserDto dto = readUser(user);
        userRepo.delete(user);
        return dto;
    }


}
