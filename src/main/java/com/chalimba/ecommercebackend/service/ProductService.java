package com.chalimba.ecommercebackend.service;

import java.util.HashSet;
import java.util.Set;

import com.chalimba.ecommercebackend.exception.BadRequestException;
import com.chalimba.ecommercebackend.exception.NotFoundException;
import com.chalimba.ecommercebackend.model.Category;
import com.chalimba.ecommercebackend.model.Product;
import com.chalimba.ecommercebackend.repository.ProductRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Set<Product> findAllProducts() {
        return new HashSet<>(productRepository.findAll());
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The product could not be found"));
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

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

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void addCategoryToProduct(Long productId, Long categoryId) {
        Product product = findProductById(productId);
        Category category = categoryService.findCategoryById(categoryId);
        product.addCategory(category);
        categoryService.saveCategory(category);
        productRepository.save(product);
    }

    public void removeCategoryFromProduct(Long productId, Long categoryId) {
        Product product = findProductById(productId);
        Category category = categoryService.findCategoryById(categoryId);
        product.removeCategory(category);
        categoryService.saveCategory(category);
        productRepository.save(product);
    }
}
