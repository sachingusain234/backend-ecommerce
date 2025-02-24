package com.neonriot.Spring_security_jwt.service;

import com.neonriot.Spring_security_jwt.entity.*;
import com.neonriot.Spring_security_jwt.repository.OrderRepository;
import com.neonriot.Spring_security_jwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    private static final PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
    public boolean saveNewUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        }catch(Exception e){
            log.error("User may already exists");
            return false;
        }
    }
    public boolean usernameExists(String username) {
        return userRepository.findByUserName(username) != null;
    }
    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));;
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);

    }
    public void deleteByUserName(String username){
        userRepository.deleteByUserName(username);
    }
    public User getUserDetails(String userName) {
        return userRepository.findByUserName(userName);
    }
    public User updatedUser(String username,User updatedUser){
        User user = userRepository.findByUserName(username);
        if(user!=null){
            if(updatedUser.getPassword()!=null && !updatedUser.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getRoles() != null) {
                user.setRoles(updatedUser.getRoles()); // Update roles
            }
            return userRepository.save(user);
        }return null;
    }
    public void addCartItemToUserCart(CartItem cartItem) {
        // Logic to add the CartItem to the user's cart (e.g., find the user, get the cart, and add the item)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        if (user != null) {
            // Get the list of cart items from the user's cart (which is a List<CartItem>)
            List<CartItem> cartItems = user.getCart();

            // Add the new CartItem to the user's cart
            cartItems.add(cartItem);

            // Save the updated user entity with the new cart item
            userRepository.save(user);  // This will save the updated List<CartItem> in the user's cart

            // Optionally, return a response or log the result
            System.out.println("Cart item added to user: " + username);
        } else {
            // Handle the case where the user is not found
            throw new RuntimeException("User not found");
        }
    }
    public List<Order> getUserOrders(ObjectId userId){
        return orderRepository.findByUserId(userId);
    }

}
