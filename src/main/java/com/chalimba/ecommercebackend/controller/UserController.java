package com.chalimba.ecommercebackend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.service.UserService;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * This controller class handles requests for the user resource.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * This method accepts requests to fetch the current user.
     * 
     * @param principal the currently authenticated principal
     * @param request   the http request
     * @return a response entity containing the user
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Principal principal, HttpServletRequest request) {
        UserDto userDto = userService.findUserByEmail(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    /**
     * This method accepts requests to fetch a user by id.
     * 
     * @param id the id of the user
     * @return a response entity containing the user
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    /**
     * This method accepts requests to fetch all users.
     * 
     * @return a response entity containing all users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        Set<UserDto> users = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * This method accepts requests to update a user by id.
     * 
     * @param id      the id of the user
     * @param userDto the request payload with the updated values
     * @return a response entity containing the updated user
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.findAndUpdateUserById(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    /**
     * This method accepts requests to update the current user.
     * 
     * @param userDto   the request payload with the updated valuse
     * @param principal the currently authenticated principal
     * @return a response entity containing the updated user
     */
    @PutMapping("/")
    public ResponseEntity<?> updateCurrentUser(@RequestBody UserDto userDto, Principal principal) {
        UserDto currentUser = userService.findUserByEmail(principal.getName());
        UserDto updatedUser = userService.findAndUpdateUserById(currentUser.getId(), userDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    /**
     * This method accepts requests to delete a user by id.
     * 
     * @param id the id of the user
     * @return an empty response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
