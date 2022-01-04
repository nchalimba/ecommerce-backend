package com.chalimba.ecommercebackend.controller;

import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.chalimba.ecommercebackend.config.JwtUtil;
import com.chalimba.ecommercebackend.dto.LoginDto;
import com.chalimba.ecommercebackend.dto.TokenDto;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.model.User;
import com.chalimba.ecommercebackend.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * This controller class handles requests for authentication.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.token.validity.access-token}")
    private long ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.token.validity.refresh-token}")
    private long REFRESH_TOKEN_VALIDITY;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto, HttpServletResponse response)
            throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = jwtUtil.generateToken(authorities, loginDto.getEmail(), ACCESS_TOKEN_VALIDITY);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        if (loginDto.getRemember() != null && loginDto.getRemember()) {
            String refreshToken = jwtUtil.generateToken(authorities, loginDto.getEmail(), REFRESH_TOKEN_VALIDITY);
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");

            response.addCookie(refreshTokenCookie);
        }
        return ResponseEntity.ok(new TokenDto(accessToken));
    }

    @PostMapping("/register")
    public User saveUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie deleteAccessTokenCookie = new Cookie("accessToken", null);
        deleteAccessTokenCookie.setHttpOnly(true);
        deleteAccessTokenCookie.setPath("/");
        deleteAccessTokenCookie.setMaxAge(0);
        Cookie deleteRefreshTokenCookie = new Cookie("refreshToken", null);
        deleteRefreshTokenCookie.setHttpOnly(true);
        deleteRefreshTokenCookie.setPath("/");
        deleteRefreshTokenCookie.setMaxAge(0);
        response.addCookie(deleteAccessTokenCookie);
        response.addCookie(deleteRefreshTokenCookie);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/userping")
    public String userPing() {
        return "Any User Can Read This";
    }

}
