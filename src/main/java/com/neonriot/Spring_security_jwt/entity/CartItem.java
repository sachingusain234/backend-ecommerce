package com.neonriot.Spring_security_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Cart-Items")
public class CartItem {
    @Id
    private String name;
    private int quantity;
    private BigDecimal price;
    private String imageUrl;
    //private String name;
}
