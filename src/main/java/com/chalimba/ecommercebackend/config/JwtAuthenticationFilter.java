package com.chalimba.ecommercebackend.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.token.validity.access-token}")
    private long ACCESS_TOKEN_VALIDITY;

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (req.getCookies() == null) {
            log.info("Not authenticated");
            chain.doFilter(req, res);
            return;
        }
        String accessToken = Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("accessToken"))
                .map(Cookie::getValue).findAny().orElse(null);
        String refreshToken = Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue).findAny().orElse(null);

        if (accessToken == null) {
            log.info("Could not find access token");
            chain.doFilter(req, res);
            return;
        }
        String username = null;

        try {
            username = jwtUtil.getUsernameFromToken(accessToken);
        } catch (IllegalArgumentException e) {
            log.error("An error occurred while fetching Username from the access token", e);
        } catch (ExpiredJwtException e) {
            log.error("The access token has expired", e);
            accessToken = reIssueAccessToken(res, refreshToken);
            if (accessToken != null) {

                username = jwtUtil.getUsernameFromToken(accessToken);
            }
        } catch (SignatureException e) {
            log.error("Authentication Failed. Username or Password not valid.", e);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthenticationToken(accessToken,
                        SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                log.info("Authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }

    private String reIssueAccessToken(HttpServletResponse response, String refreshToken) {
        if (refreshToken == null) {
            return null;
        }
        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            String role = jwtUtil.getRoleFromToken(refreshToken);
            // generate new accessToken
            String newAccessToken = jwtUtil.generateToken(role, username, ACCESS_TOKEN_VALIDITY);
            Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);
            return newAccessToken;
        } catch (IllegalArgumentException e) {
            log.error("An error occurred while fetching Username from the refresh token", e);
        } catch (ExpiredJwtException e) {
            log.warn("The refresh token has expired");
        } catch (SignatureException e) {
            log.error("Authentication Failed. Username or Password not valid.", e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }
}