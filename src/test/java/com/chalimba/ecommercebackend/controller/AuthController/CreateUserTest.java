package com.chalimba.ecommercebackend.controller.AuthController;

import com.chalimba.ecommercebackend.TestData;
import com.chalimba.ecommercebackend.TestUtils;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class CreateUserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturn201_WhenValidRequest() throws Exception {
        // given
        UserDto userDto = TestData.USER_DTO;
        // then
        mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(userDto)))
                .andDo(print()).andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400_WhenEmailDuplicate() throws Exception {
        // given
        userRepository.save(TestData.TEST_USER);
        UserDto userDto = TestData.USER_DTO;
        // then
        mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(userDto)))
                .andDo(print()).andExpect(status().isBadRequest());
    }

}
