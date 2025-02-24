package com.neonriot.Spring_security_jwt.controller;

import com.neonriot.Spring_security_jwt.entity.*;
import com.neonriot.Spring_security_jwt.repository.UserRepository;
import com.neonriot.Spring_security_jwt.service.OrderService;
import com.neonriot.Spring_security_jwt.service.ProductService;
import com.neonriot.Spring_security_jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("health-order")
    public String healthCheck(){
        return "I am working fine";
    }
    @GetMapping
    public ResponseEntity<?> findUserOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);
        if (user != null && user.getOrders() != null) {
            // Return the cart items in the response
            List<Order> order = user.getOrders();
            return ResponseEntity.ok(order);
        } else {
            // If no cart exists or user is not found, return a 404 or empty response
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<?> addNewOrder() {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);

        // If user not found, return NOT_FOUND status
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // If the cart is empty, return BAD_REQUEST status
        if (user.getCart() == null || user.getCart().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty");
        }

        // Initialize order items and total amount
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Iterate over the cart items and process them
        for (CartItem cartItem : user.getCart()) {
            // Fetch product details using the productId from cart item
            Optional<Products> productOptional = productService.getProductById(cartItem.getName());

            // If product not found, return NOT_FOUND status
            if (!productOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

            // Retrieve the product and create OrderItem
            Products product = productOptional.get();
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(product.getName());  // Set product ID
            orderItem.setQuantity(cartItem.getQuantity());  // Set quantity from cart item
            orderItem.setPrice(product.getPrice());  // Set price from product
            orderItems.add(orderItem);  // Add to the order items list

            // Update the total amount by adding the cost of the current cart item
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Create new Order and set necessary details
        Order newOrder = new Order();
        newOrder.setUserId(user.getId());  // Set userId instead of user object
        newOrder.setItems(orderItems);  // Set the list of order items
        newOrder.setTotalAmount(totalAmount);  // Set the calculated total amount
        newOrder.setOrderDate(LocalDateTime.now());  // Set the current time as order date
        newOrder.setStatus("PENDING");  // Set the order status as "PENDING"

        // Save the new order
        orderService.saveOrder(newOrder);

        // Add the new order to the user's orders list and save the user
        user.getOrders().add(newOrder);
        userRepository.save(user);

        // Clear the user's cart and save the updated user entity
        user.getCart().clear();
        userRepository.save(user);

        // Return success response
        return ResponseEntity.status(HttpStatus.CREATED).body("Order placed successfully");
    }

}
