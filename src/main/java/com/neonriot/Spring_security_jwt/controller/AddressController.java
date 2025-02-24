package com.neonriot.Spring_security_jwt.controller;

import com.neonriot.Spring_security_jwt.dto.AddressDTO;
import com.neonriot.Spring_security_jwt.entity.Address;
import com.neonriot.Spring_security_jwt.entity.User;
import com.neonriot.Spring_security_jwt.repository.AddressRepository;
import com.neonriot.Spring_security_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/addresses")
@RequiredArgsConstructor
public class AddressController {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    // Add a new address
    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody Address address, Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Initialize addresses list if null
            if (user.getAddresses() == null) {
                user.setAddresses(new ArrayList<>());
            }

            // Generate new ObjectId for the address
            //address.setId(new ObjectId());

            // If this is the first address or marked as default, make it default
            if (user.getAddresses().isEmpty() || address.isDefault()) {
                user.getAddresses().forEach(addr -> addr.setDefault(false));
            }

            // Save the address first
            addressRepository.save(address);

            // Add the address to user and save
            user.getAddresses().add(address);
            userRepository.save(user);

            return ResponseEntity.ok(user.getAddresses());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding address: " + e.getMessage());
        }
    }


    // Get all addresses for a user
    @GetMapping
    public ResponseEntity<?> getAddresses(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Convert the list of Address objects to AddressDTO objects
            List<AddressDTO> addressDTOs = user.getAddresses().stream()
                    .map(address -> new AddressDTO(
                            address.getId() != null ? address.getId().toHexString() : null,  // Convert ObjectId to String
                            address.getType(),
                            address.getAddress(),
                            address.getCity(),
                            address.getState(),
                            address.getPincode(),
                            address.isDefault()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(addressDTOs != null ? addressDTOs : new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching addresses: " + e.getMessage());
        }
    }



    // Update an address
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable String addressId,
            @RequestBody Address updatedAddress,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            if (user.getAddresses() == null) {
                return ResponseEntity.notFound().build();
            }

            List<Address> addresses = user.getAddresses();
            boolean addressFound = false;

            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getId().toString().equals(addressId)) {
                    updatedAddress.setId(new ObjectId(addressId));

                    if (updatedAddress.isDefault()) {
                        addresses.forEach(addr -> addr.setDefault(false));
                    }

                    addresses.set(i, updatedAddress);
                    addressFound = true;
                    break;
                }
            }

            if (!addressFound) {
                return ResponseEntity.notFound().build();
            }

            userRepository.save(user);
            return ResponseEntity.ok(user.getAddresses());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating address: " + e.getMessage());
        }
    }

    // Delete an address
    @DeleteMapping("/{address}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable String address,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            if (user.getAddresses() == null) {
                return ResponseEntity.notFound().build();
            }

            boolean removed = user.getAddresses().removeIf(
                    addr -> addr.getAddress().equals(address)
            );

            if (!removed) {
                return ResponseEntity.notFound().build();
            }

            // If we removed a default address and there are other addresses,
            // make the first one default
            if (!user.getAddresses().isEmpty() &&
                    user.getAddresses().stream().noneMatch(Address::isDefault)) {
                user.getAddresses().get(0).setDefault(true);
            }

            userRepository.save(user);
            return ResponseEntity.ok(user.getAddresses());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting address: " + e.getMessage());
        }
    }

    // Set an address as default
    @PutMapping("/{addressId}/set-default")
    public ResponseEntity<?> setDefaultAddress(
            @PathVariable String addressId,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUserName(username);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            if (user.getAddresses() == null) {
                return ResponseEntity.notFound().build();
            }

            List<Address> addresses = user.getAddresses();
            boolean addressFound = false;

            for (Address address : addresses) {
                if (address.getId().toString().equals(addressId)) {
                    address.setDefault(true);
                    addressFound = true;
                } else {
                    address.setDefault(false);
                }
            }

            if (!addressFound) {
                return ResponseEntity.notFound().build();
            }

            userRepository.save(user);
            return ResponseEntity.ok(user.getAddresses());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error setting default address: " + e.getMessage());
        }
    }
}