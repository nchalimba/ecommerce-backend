package com.chalimba.ecommercebackend.repository;

import java.util.Optional;

import com.chalimba.ecommercebackend.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface contains the api for database operations of the category
 * table.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByTitle(String title);
}
