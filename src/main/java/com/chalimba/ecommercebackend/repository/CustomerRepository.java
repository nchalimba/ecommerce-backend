package com.chalimba.ecommercebackend.repository;

import com.chalimba.ecommercebackend.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    long deleteByUserId(Long id);
}
