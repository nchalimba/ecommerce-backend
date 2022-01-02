package com.chalimba.ecommercebackend.controller;

import com.chalimba.ecommercebackend.config.JwtUtil;
import com.chalimba.ecommercebackend.dto.LoginDto;
import com.chalimba.ecommercebackend.dto.TokenDto;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.model.User;
import com.chalimba.ecommercebackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.token.validity.access-token}")
    private long ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.token.validity.refresh-token}")
    private long REFRESH_TOKEN_VALIDITY;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateToken(authentication, ACCESS_TOKEN_VALIDITY);
        if (loginDto.getRemember()) {
            String refreshToken = jwtUtil.generateToken(authentication, REFRESH_TOKEN_VALIDITY);
            return ResponseEntity.ok(new TokenDto(accessToken, refreshToken));
        }
        return ResponseEntity.ok(new TokenDto(accessToken, null));
    }

    @PostMapping("/register")
    public User saveUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/userping")
    public String userPing() {
        return "Any User Can Read This";
    }
}
