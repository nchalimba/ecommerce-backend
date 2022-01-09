package com.chalimba.ecommercebackend.controller;

import java.util.Set;

import com.chalimba.ecommercebackend.model.Product;
import com.chalimba.ecommercebackend.service.ProductService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * This controller class handles requests for the product resource.
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * This method accepts requests to fetch all products
     * 
     * @return a response entity containing all products
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllProducts() {
        Set<Product> products = productService.findAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    /**
     * This method accepts requests to fetch a category by id.
     * 
     * @param id the id of the product
     * @return a response entity containing the product
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product product = productService.findProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    /**
     * This method accepts requests to create a new product.
     * 
     * @param product the request payload with the new product
     * @return a response entity containing the saved product
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
    }

    /**
     * This method accepts requests to update a product by id.
     * 
     * @param id      the id of the product
     * @param product the request payload with the updated values
     * @return a response entity containing the updated product
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.findAndUpdateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    /**
     * This method accepts requests to delete a product by id.
     * 
     * @param id the id of the product
     * @return an empty response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * This method accepts requests to add or to remove a given category to / from a
     * product by id
     * 
     * @param productId  the product id from which the category should be
     *                   removed / added
     * @param categoryId the category id that should be added / removed
     * @param delete     a boolean determining whether the category should be added
     *                   or removed
     * @return an empty response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> addOrRemoveCategoryToProduct(@RequestParam Long productId, @RequestParam Long categoryId,
            @RequestParam(required = false) Boolean delete) {
        if (delete == null || !delete) {
            productService.addCategoryToProduct(productId, categoryId);
        } else {
            productService.removeCategoryFromProduct(productId, categoryId);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
