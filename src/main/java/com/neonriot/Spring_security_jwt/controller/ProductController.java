package com.neonriot.Spring_security_jwt.controller;

import com.neonriot.Spring_security_jwt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.neonriot.Spring_security_jwt.repository.ProductRepository;
import com.neonriot.Spring_security_jwt.entity.Products;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts(){
        return ResponseEntity.ok(productRepository.findAll());
    }
    @PostMapping
    public ResponseEntity<?> addNewProduct(@RequestBody Products product){
        if (product.getCreatedDate() == null) {
            product.setCreatedDate(LocalDateTime.now());  // Set the current date and time
        }
        productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/{name}")
    public ResponseEntity<?> getProductByProductName(@PathVariable String name) {
        Products product = productService.getProductByName(name);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductByCategory(@PathVariable String category){
        List<Products> products = productRepository.findByCategory(category);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
