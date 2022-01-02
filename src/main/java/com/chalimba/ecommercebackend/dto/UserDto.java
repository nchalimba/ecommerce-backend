package com.chalimba.ecommercebackend.dto;

import com.chalimba.ecommercebackend.model.Customer;
import com.chalimba.ecommercebackend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class UserDto {
    private String email;
    private String password;
    private String phone;
    private String firstName;
    private String lastName;

    @JsonIgnore
    public User getUser() {
        Customer customer = Customer.builder().email(email).firstName(firstName).lastName(lastName).phone(phone)
                .build();
        return User.builder().email(email).password(password).customer(customer).build();
    }

    public UserDto(User user) {
        this.email = user.getEmail();
        Customer customer = user.getCustomer();
        if (customer != null) {
            this.phone = customer.getPhone();
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
        }
    }
}
