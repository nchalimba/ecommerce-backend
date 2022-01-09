package com.chalimba.ecommercebackend.service;

import java.util.HashSet;
import java.util.Set;

import com.chalimba.ecommercebackend.exception.BadRequestException;
import com.chalimba.ecommercebackend.exception.NotFoundException;
import com.chalimba.ecommercebackend.model.Category;
import com.chalimba.ecommercebackend.model.Product;
import com.chalimba.ecommercebackend.repository.ProductRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * This class contains the business logic for product-related requests.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    /**
     * This method fetches all products from the database.
     * 
     * @return a hashset of all products
     */
    public Set<Product> findAllProducts() {
        return new HashSet<>(productRepository.findAll());
    }

    /**
     * This method fetches a product from the database with a given id.
     * 
     * @param id the id of the product
     * @return the product
     * @throws NotFoundException if the product does not exist
     */
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The product could not be found"));
    }

    /**
     * This method saves a new product to the database.
     * 
     * @param product the product to be saved
     * @return the saved product
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * This method fetches and updates a product with a given id.
     * 
     * @param id      the id of the product
     * @param product the new values for the product
     * @return the updated product
     * @throws NotFoundException   if the product does not exist
     * @throws BadRequestException if the updated title is already in use
     */
    public Product findAndUpdateProduct(Long id, Product product) {
        Product savedProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The product could not be found"));

        if (!product.getTitle().equals(savedProduct.getTitle()))
            productRepository.findByTitle(product.getTitle()).ifPresent(otherCategory -> {
                throw new BadRequestException("The title is already used");
            });

        savedProduct.setTitle(product.getTitle());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setImageUrl(product.getImageUrl());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setQuantity(product.getQuantity());
        return productRepository.save(savedProduct);
    }

    /**
     * This method deletes a product from the database with a given id.
     * 
     * @param id the id of the product
     * @throws NotFoundException if the product does not exist
     */
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The product could not be found");
        }
    }

    /**
     * This method adds a category to a product.
     * 
     * @param productId  the id of the product
     * @param categoryId the id of the category
     */
    public void addCategoryToProduct(Long productId, Long categoryId) {
        Product product = findProductById(productId);
        Category category = categoryService.findCategoryById(categoryId);
        product.addCategory(category);
        categoryService.saveCategory(category);
        productRepository.save(product);
    }

    /**
     * This method removes a category from a product.
     * 
     * @param productId  the id of the product
     * @param categoryId the id of the category
     */
    public void removeCategoryFromProduct(Long productId, Long categoryId) {
        Product product = findProductById(productId);
        Category category = categoryService.findCategoryById(categoryId);
        product.removeCategory(category);
        categoryService.saveCategory(category);
        productRepository.save(product);
    }
}
