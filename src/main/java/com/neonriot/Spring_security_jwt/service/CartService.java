package com.neonriot.Spring_security_jwt.service;

import com.neonriot.Spring_security_jwt.entity.CartItem;
import com.neonriot.Spring_security_jwt.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CartService{
    @Autowired
    private CartRepository cartRepository;

    public void addCartItem(CartItem item) {

        // Save the CartItem to MongoDB
        cartRepository.save(item);
    }
}
