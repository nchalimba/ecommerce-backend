package com.chalimba.ecommercebackend.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chalimba.ecommercebackend.TestData;
import com.chalimba.ecommercebackend.repository.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(TestData.TEST_USER);
    }

    @WithMockUser(username = TestData.USERNAME, roles = "ADMIN")
    @Test
    @Disabled
    void getUserById_ValidRequest_ReturnUser() throws Exception {
        // TODO: Mock
    }

    @Test
    @Disabled
    void shouldDeleteUserById() {
        // TODO: Mock
    }

    @WithMockUser(username = TestData.USERNAME, roles = "ADMIN")
    @Test
    void getAllUsers_Authenticated_ReturnAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/")).andDo(print()).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @WithMockUser(username = TestData.USERNAME)
    @Test
    void getCurrentUser_Authenticated_ReturnCurrentUser() throws Exception {
        mockMvc.perform(get("/api/user/current")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getCurrentUser_NotAuthenticated_Return401() throws Exception {
        mockMvc.perform(get("/api/user/current")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Disabled
    @Test
    void testUpdateCurrentUser() {

    }

    @Disabled
    @Test
    void testUpdateUserById() {

    }
}
