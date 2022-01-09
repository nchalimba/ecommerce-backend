package com.chalimba.ecommercebackend.repository;

import java.util.Optional;

import com.chalimba.ecommercebackend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface contains the api for database operations of the user
 * table.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
