package com.chalimba.ecommercebackend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chalimba.ecommercebackend.exception.BadRequestException;
import com.chalimba.ecommercebackend.exception.NotFoundException;
import com.chalimba.ecommercebackend.model.Category;
import com.chalimba.ecommercebackend.repository.CategoryRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Set<Category> findAllCategories() {
        return new HashSet<>(categoryRepository.findAll());
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The category could not be found"));
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

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

    public void deleteCategoryById(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The category could not be found");
        }
    }
}
