package com.neonriot.Spring_security_jwt.repository;

import com.neonriot.Spring_security_jwt.entity.CartItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<CartItem, ObjectId> {
}
