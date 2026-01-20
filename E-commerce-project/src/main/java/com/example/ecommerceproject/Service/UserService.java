package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.User;
import com.example.ecommerceproject.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    // create user
    UserDto createUser(UserDto userDto);
    // get All user
    List<UserDto> getAllUsers();
    //get single user
    UserDto getById(Long id);

    //update user
    UserDto updateByUser(Long id, UserDto userDto);
    User findByEmail(String email);
    // delete user
    UserDto deleteUser(Long id);



    /////////////////////////////////////////////////////////////////////////

}

/*
    //  customs query based

    // create user query
     User create(User user);

     // get all user query
    List<User> getAll();

    // update user query
    User updateByUserId(User user);

    // delete user query
    User deleteByUserId(Long id);


}*/