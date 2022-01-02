package com.chalimba.ecommercebackend.repository;

import java.util.Optional;

import com.chalimba.ecommercebackend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
