package com.neonriot.Spring_security_jwt.controller;

import com.neonriot.Spring_security_jwt.entity.CartItem;
import com.neonriot.Spring_security_jwt.entity.Products;
import com.neonriot.Spring_security_jwt.entity.User;
import com.neonriot.Spring_security_jwt.repository.CartRepository;
import com.neonriot.Spring_security_jwt.repository.UserRepository;
import com.neonriot.Spring_security_jwt.service.ProductService;
import com.neonriot.Spring_security_jwt.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("https://soundverse-ruddy.vercel.app/")
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @GetMapping("/health-cart")
    public String healthCheck(){
        return "I am working fine";
    }
    @GetMapping("/cart")
    public ResponseEntity<?> getCartItems(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);
        if (user != null && user.getCart() != null) {
            // Return the cart items in the response
            List<CartItem> cartItems = user.getCart();
            return ResponseEntity.ok(cartItems);
        } else {
            // If no cart exists or user is not found, return a 404 or empty response
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/cart")
    public ResponseEntity<?> addCartItem(@RequestBody CartItem cartItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserDetails(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Products> productOptional = Optional.ofNullable(productService.getProductByName(cartItem.getName()));
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        Products product = productOptional.get();
        boolean exist = false;
        for(CartItem item: user.getCart()){
            if(item.getName().equals(cartItem.getName())){
                item.setQuantity(item.getQuantity()+1);
                cartRepository.save(item);
                exist = true;
                break;
            }
        }
        if(!exist){
            user.getCart().add(cartItem);
            System.out.println("Cart item added to user's cart: " + cartItem);  // Add a log
            cartRepository.save(cartItem);
        }// Save the CartItem to the DB
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Cart item added successfully");
    }
    @DeleteMapping("/cart/{name}")
    public ResponseEntity<?> deleteCartItem(@PathVariable String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (user.getCart() == null || user.getCart().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty");
        }
        Optional<CartItem> cartItemToRemove = user.getCart().stream()
                .filter(cartItem -> cartItem.getName().equals(name))  // Compare the ObjectId
                .findFirst();

        if (!cartItemToRemove.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found");
        }
        user.getCart().remove(cartItemToRemove.get());
        userRepository.save(user);
        return  ResponseEntity.status(HttpStatus.OK).body("Cart item removed sucessfully");
    }
    @PutMapping("/cart/{name}")
    public ResponseEntity<?> decreaseCartItemQuantity(@PathVariable String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserDetails(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Products> productOptional = Optional.ofNullable(productService.getProductByName(name));
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        Products product = productOptional.get();

        boolean exist = false;
        for (CartItem item : user.getCart()) {
            if (item.getName().equals(name)) {
                // If the quantity is greater than 1, decrease it
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    cartRepository.save(item);  // Save the updated cart item
                } else {
                    // If quantity is 1, we may want to remove the item entirely
                    user.getCart().remove(item);
                    cartRepository.delete(item);  // Remove the cart item from the DB
                }
                exist = true;
                break;
            }
        }

        if (!exist) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart");
        }

        userRepository.save(user);  // Save the updated user cart

        return ResponseEntity.status(HttpStatus.OK).body("Cart item quantity decreased successfully");
    }
}
