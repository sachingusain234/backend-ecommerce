package com.neonriot.Spring_security_jwt.controller;

import com.neonriot.Spring_security_jwt.entity.CardDetails;
import com.neonriot.Spring_security_jwt.entity.User;
import com.neonriot.Spring_security_jwt.repository.AddressRepository;
import com.neonriot.Spring_security_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;
@RestController
@RequestMapping("/user/card-details")
@RequiredArgsConstructor
public class CardDetailsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody CardDetails cardDetails, Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Initialize cards list if null
            if (user.getCardDetails() == null) {
                user.setCardDetails(new ArrayList<>());
            }

            // If this is the first card or marked as default, make it default
            if (user.getCardDetails().isEmpty() || cardDetails.isDefault()) {
                user.getCardDetails().forEach(card -> card.setDefault(false));
            }

            // Save the card details first
            //cardDetailsRepo.save(cardDetails);

            // Add the card to user and save
            user.getCardDetails().add((CardDetails) cardDetails);
            userRepository.save(user);

            return ResponseEntity.ok(user.getCardDetails());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding card: " + e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> getCards(Authentication authentication) {
        try {
            // Retrieve the username from the authentication object
            String username = authentication.getName();

            // Find the user based on the username
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // If the user has no cards, return a message indicating no cards
            if (user.getCardDetails() == null || user.getCardDetails().isEmpty()) {
                return ResponseEntity.ok("No cards found for this user.");
            }

            // Return the list of card details
            return ResponseEntity.ok(user.getCardDetails());
        } catch (Exception e) {
            // Handle any exceptions and return an error message
            return ResponseEntity.badRequest().body("Error retrieving cards: " + e.getMessage());
        }
    }

}
