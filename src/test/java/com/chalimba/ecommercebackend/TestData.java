package com.chalimba.ecommercebackend;

import com.chalimba.ecommercebackend.dto.LoginDto;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.model.Customer;
import com.chalimba.ecommercebackend.model.User;

public class TestData {
    public static final String USERNAME = "testuser@gmail.com";
    private static final Customer TEST_CUSTOMER = new Customer(null, USERNAME, "08912345678", "Test",
            "User", null);
    public static final User TEST_USER = new User(null, USERNAME, "1234", "CUSTOMER", TEST_CUSTOMER);
    public static final UserDto USER_DTO = new UserDto(null, USERNAME, "1234", "12345678", "Test", "User");
    public static final LoginDto LOGIN_DTO = new LoginDto(USERNAME, "1234", true);

}
