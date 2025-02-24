package com.neonriot.Spring_security_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "address")
public class Address {
    @Id
    private ObjectId id;
    private String type;        // e.g., "Home", "Work", etc.
    private String address;     // Street address
    private String city;
    private String state;
    private String pincode;
    private boolean isDefault;
}