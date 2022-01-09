package com.chalimba.ecommercebackend.service;

import java.util.HashSet;
import java.util.Set;

import com.chalimba.ecommercebackend.exception.BadRequestException;
import com.chalimba.ecommercebackend.exception.NotFoundException;
import com.chalimba.ecommercebackend.model.Category;
import com.chalimba.ecommercebackend.repository.CategoryRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * This class contains the business logic for category-related requests.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * This method fetches all categories from the database.
     * 
     * @return a hashset of all categories
     */
    public Set<Category> findAllCategories() {
        return new HashSet<>(categoryRepository.findAll());
    }

    /**
     * This method fetches a category from the database with a given id.
     * 
     * @param id the id of the category
     * @return the category
     * @throws NotFoundException if the category does not exist
     */
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The category could not be found"));
    }

    /**
     * This method saves a new category to the database.
     * 
     * @param category the category to be saved
     * @return the saved category
     */
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * This method fetches and updates a category with a given id.
     * 
     * @param id       the id of the category
     * @param category the new values for the category
     * @return the updated category
     * @throws NotFoundException   if the category does not exist
     * @throws BadRequestException if the updated title is already in use
     */
    public Category findAndUpdateCategory(Long id, Category category) {
        Category savedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The category could not be found"));

        if (!category.getTitle().equals(savedCategory.getTitle()))
            categoryRepository.findByTitle(category.getTitle()).ifPresent(otherCategory -> {
                throw new BadRequestException("The title is already used");
            });

        savedCategory.setTitle(category.getTitle());
        savedCategory.setImageUrl(category.getImageUrl());
        return categoryRepository.save(savedCategory);
    }

    /**
     * This method deletes a category from the database with a given id
     * 
     * @param id the id of the category
     * @throws NotFoundException if the category does not exist
     */
    public void deleteCategoryById(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The category could not be found");
        }
    }
}
