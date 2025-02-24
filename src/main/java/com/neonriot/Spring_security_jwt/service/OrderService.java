package com.neonriot.Spring_security_jwt.service;

import com.neonriot.Spring_security_jwt.entity.Order;
import com.neonriot.Spring_security_jwt.repository.OrderRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Create or Update
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Read
    public Optional<Order> getOrderById(ObjectId id) {
        return orderRepository.findById(id);
    }

    // Delete
    public void deleteOrder(ObjectId id) {
        orderRepository.deleteById(id);
    }
}
