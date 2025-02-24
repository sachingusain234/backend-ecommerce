package com.neonriot.Spring_security_jwt.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.util.ArrayList;
import java.util.List;
@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique=true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private List<String> roles;
    @DBRef(lazy = false)
    private List<CartItem> cart = new ArrayList<>();
    @DBRef(lazy = false)
    private List<Order> orders = new ArrayList<>();
    private String profileUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    @DBRef
    private List<Address> addresses = new ArrayList<>();
    //@DBRef
    private List<CardDetails>  cardDetails = new ArrayList<>();
}
