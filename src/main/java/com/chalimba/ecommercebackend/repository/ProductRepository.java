package com.chalimba.ecommercebackend.repository;

import java.util.Optional;

import com.chalimba.ecommercebackend.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByTitle(String title);
}
