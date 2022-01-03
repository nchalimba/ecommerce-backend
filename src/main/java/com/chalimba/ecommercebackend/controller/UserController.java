package com.chalimba.ecommercebackend.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Principal principal, HttpServletRequest request) {
        UserDto userDto = userService.findUserByEmail(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

}
