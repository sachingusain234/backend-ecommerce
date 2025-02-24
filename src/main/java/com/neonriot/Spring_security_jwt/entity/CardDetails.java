package com.neonriot.Spring_security_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDetails {
    @Id
    private ObjectId id;
    private String cardNumber;
    private String cardHolderName;
    private String ExpiryMonth;
    private String expiryYear;
    private String cvv;
    private boolean isDefault;
}
