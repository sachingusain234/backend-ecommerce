package com.neonriot.Spring_security_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    //@DBRef(lazy = true)
    //private ObjectId product;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
