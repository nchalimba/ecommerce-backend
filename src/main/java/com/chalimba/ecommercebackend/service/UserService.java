package com.chalimba.ecommercebackend.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.chalimba.ecommercebackend.config.Roles;
import com.chalimba.ecommercebackend.dto.UserDto;
import com.chalimba.ecommercebackend.exception.BadRequestException;
import com.chalimba.ecommercebackend.exception.NotFoundException;
import com.chalimba.ecommercebackend.model.Customer;
import com.chalimba.ecommercebackend.model.User;
import com.chalimba.ecommercebackend.repository.CustomerRepository;
import com.chalimba.ecommercebackend.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        return new HashSet<>(Arrays.asList(authority));
    }

    public Set<UserDto> findAllUsers() {
        Set<User> list = new HashSet<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list.stream().map((user) -> new UserDto(user)).collect(Collectors.toSet());
    }

    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("The user could not be found"));
        return new UserDto(user);
    }

    public User saveUser(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).ifPresent(otherUser -> {
            throw new BadRequestException("The email is already used");
        });
        User user = userDto.getUser();
        user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        user.setRole(Roles.CUSTOMER.name());
        return userRepository.save(user);
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("The user could not be found"));
        return new UserDto(user);
    }

    public UserDto findAndUpdateUserById(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("The user could not be found"));
        if (!user.getEmail().equals(userDto.getEmail()))
            userRepository.findByEmail(userDto.getEmail()).ifPresent(otherUser -> {
                throw new BadRequestException("The email is already used");
            });

        user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null)
            user.setPassword(bcryptEncoder.encode(userDto.getPassword()));

        Customer customer = user.getCustomer();
        customer.setEmail(userDto.getEmail());
        customer.setFirstName(userDto.getFirstName());
        customer.setLastName(userDto.getLastName());
        customer.setPhone(userDto.getPhone());
        customerRepository.save(customer);
        return new UserDto(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        customerRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
