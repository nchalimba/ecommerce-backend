package com.chalimba.ecommercebackend.controller;

import java.util.Set;

import com.chalimba.ecommercebackend.model.Category;
import com.chalimba.ecommercebackend.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * This controller class handles requests for the category resource.
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * This method accepts requests to fetch all categories.
     * 
     * @return a response entity containing all categories
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllCategories() {
        Set<Category> categories = categoryService.findAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    /**
     * This method accepts requests to fetch a category by id.
     * 
     * @param id the id of the category
     * @return a response entity containing the category
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    /**
     * This method accepts requests to create a new category.
     * 
     * @param category the category to be persisted
     * @return a response entity with the persisted category
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    /**
     * This method accepts requests to update a category by id.
     * 
     * @param id       the id of the category
     * @param category the request payload with the updated data
     * @return a response entity containing the updated category
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryById(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.findAndUpdateCategory(id, category);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    /**
     * This method accepts requests to delete a category by id.
     * 
     * @param id the id of the category
     * @return an empty response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
