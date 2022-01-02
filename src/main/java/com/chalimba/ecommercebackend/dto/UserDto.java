package com.chalimba.ecommercebackend.dto;

import com.chalimba.ecommercebackend.model.Customer;
import com.chalimba.ecommercebackend.model.User;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String phone;
    private String firstName;
    private String lastName;

    public User getUser() {
        Customer customer = Customer.builder().email(email).firstName(firstName).lastName(lastName).phone(phone)
                .build();
        return User.builder().email(email).password(password).customer(customer).build();
    }
}
