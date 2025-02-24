package com.neonriot.Spring_security_jwt.service;

import com.neonriot.Spring_security_jwt.entity.Products;
import com.neonriot.Spring_security_jwt.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Create or Update
    public Products saveProduct(Products product) {
        return productRepository.save(product);
    }

    // Read
    public Optional<Products> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Products getProductByName(String name) {
        return productRepository.findByName(name);
    }

    // Delete
    public void deleteProduct(ObjectId id) {
        productRepository.deleteById(id);
    }
}
