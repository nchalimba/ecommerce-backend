package com.chalimba.ecommercebackend.controller.UserController;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chalimba.ecommercebackend.TestData;
import com.chalimba.ecommercebackend.repository.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest
public class GetAllUsersTest {

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
    void shouldReturn200_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/user/")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldReturn401_WhenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = TestData.USERNAME, roles = "CUSTOMER")
    @Test
    void shouldReturn403_WhenNotAdmin() throws Exception {
        mockMvc.perform(get("/api/user/")).andDo(print()).andExpect(status().isForbidden());
    }
}
