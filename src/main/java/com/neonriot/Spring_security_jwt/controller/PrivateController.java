package com.neonriot.Spring_security_jwt.controller;
import com.neonriot.Spring_security_jwt.entity.CartItem;
import com.neonriot.Spring_security_jwt.entity.Order;
import com.neonriot.Spring_security_jwt.entity.User;
import com.neonriot.Spring_security_jwt.repository.UserRepository;
import com.neonriot.Spring_security_jwt.service.UserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class PrivateController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @GetMapping("/health-check")
    public ResponseEntity<?> healtCheck(){
        return new ResponseEntity<>("I am working fine", HttpStatus.OK);
    }
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.updatedUser(username,updatedUser);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public void deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteByUserName(username);
    }
    @GetMapping("/")
    public ResponseEntity<List<Order>> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);
        if (user == null) {
            // In case the user is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Order> orders = userService.getUserOrders(user.getId());

        if (orders.isEmpty()) {
            // If no orders are found
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Return the list of orders with a 200 OK status
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PutMapping("/personal-info")
    public ResponseEntity<?> personalInfo(@RequestBody User newUser){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserDetails(username);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setProfileUrl(newUser.getProfileUrl());
        userRepository.save(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
