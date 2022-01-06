package com.chalimba.ecommercebackend.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.chalimba.ecommercebackend.TestData;
import com.chalimba.ecommercebackend.TestUtils;
import com.chalimba.ecommercebackend.dto.LoginDto;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.model.User;
import com.chalimba.ecommercebackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@WebMvcTest(controllers = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.chalimba.ecommercebackend.config.*") })
        */
@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_ValidUser_Return201() throws Exception {
        // given
        UserDto userDto = TestData.USER_DTO;
        // then
        mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(userDto)))
                .andDo(print()).andExpect(status().isCreated());

    }

    @Test
    void login_ValidCredentials_Return200() throws Exception {
        // given
        LoginDto loginDto = TestData.LOGIN_DTO;
        User user = TestData.TEST_USER;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // then
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(loginDto)))
                .andDo(print()).andExpect(status().isOk());

    }

    @Test
    void login_InvalidCredentials_Return400() throws Exception {
        // given
        LoginDto loginDto = TestData.LOGIN_DTO;

        // then
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(loginDto)))
                .andDo(print()).andExpect(status().isBadRequest());

    }

    @Test
    void logout_Return200() throws Exception {
        mockMvc.perform(post("/api/auth/logout")).andDo(print()).andExpect(status().isOk());
    }

}
