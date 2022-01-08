package com.chalimba.ecommercebackend.controller.AuthController;

import com.chalimba.ecommercebackend.TestData;
import com.chalimba.ecommercebackend.TestUtils;
import com.chalimba.ecommercebackend.dto.LoginDto;
import com.chalimba.ecommercebackend.model.User;
import com.chalimba.ecommercebackend.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {
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
    void shouldReturn200_WhenValidCredentials() throws Exception {
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
    void shouldReturn400_WhenInvalidCredentials() throws Exception {
        // given
        LoginDto loginDto = TestData.LOGIN_DTO;

        // then
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(loginDto)))
                .andDo(print()).andExpect(status().isBadRequest());

    }
}
