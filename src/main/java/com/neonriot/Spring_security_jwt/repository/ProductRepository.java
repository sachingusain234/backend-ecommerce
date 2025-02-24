package com.neonriot.Spring_security_jwt.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.neonriot.Spring_security_jwt.entity.Products;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Products, ObjectId> {
    List<Products> findByCategory(String category);
    void deleteById(ObjectId id);
    Products findByName(String name);

    Optional<Products> findById(String id);
}