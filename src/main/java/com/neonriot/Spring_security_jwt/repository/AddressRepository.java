package com.neonriot.Spring_security_jwt.repository;

import com.neonriot.Spring_security_jwt.entity.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    Optional<Address> findById(ObjectId id);
}
