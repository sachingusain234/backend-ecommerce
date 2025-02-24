package com.neonriot.Spring_security_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String id;
    private String type;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private boolean isDefault;
}