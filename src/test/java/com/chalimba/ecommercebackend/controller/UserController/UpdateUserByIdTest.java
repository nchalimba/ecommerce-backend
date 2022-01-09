package com.chalimba.ecommercebackend.controller.UserController;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chalimba.ecommercebackend.TestData;
import com.chalimba.ecommercebackend.TestUtils;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.exception.NotFoundException;
import com.chalimba.ecommercebackend.repository.UserRepository;
import com.chalimba.ecommercebackend.service.UserService;

@AutoConfigureMockMvc
@SpringBootTest
public class UpdateUserByIdTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(TestData.TEST_USER);
    }

    @WithMockUser(username = TestData.USERNAME, roles = "ADMIN")
    @Test
    void shouldReturn200_WhenValidRequest() throws Exception {
        // given
        UserDto userDto = TestData.USER_DTO;
        userDto.setFirstName("New first name");
        // then
        when(userService.findAndUpdateUserById(anyLong(), any())).thenReturn(userDto);
        mockMvc.perform(put("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(userDto))).andDo(print())
                .andExpect(status().isOk());
        verify(userService).findAndUpdateUserById(anyLong(), any());
    }

    @WithMockUser(username = TestData.USERNAME, roles = "ADMIN")
    @Test
    void shouldReturn404_WhenFalseId() throws Exception {
        // given
        UserDto userDto = TestData.USER_DTO;
        userDto.setFirstName("New first name");
        // then
        when(userService.findAndUpdateUserById(anyLong(), any()))
                .thenThrow(new NotFoundException("The user could not be found"));
        mockMvc.perform(put("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(userDto))).andDo(print())
                .andExpect(status().isNotFound());
        verify(userService).findAndUpdateUserById(anyLong(), any());
    }

    @Test
    void shouldReturn401_WhenUnauthorized() throws Exception {
        mockMvc.perform(put("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(TestData.USER_DTO))).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = TestData.USERNAME, roles = "CUSTOMER")
    @Test
    void shouldReturn403_WhenNotAdmin() throws Exception {
        mockMvc.perform(put("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(TestData.USER_DTO))).andDo(print()).andExpect(status().isForbidden());
    }

}
