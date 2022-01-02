package com.chalimba.ecommercebackend.service;

import java.util.List;

import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.model.User;

public interface UserService {
    User saveUser(UserDto userDto);

    List<User> findAllUsers();

    User findUserByEmail(String email);
}
