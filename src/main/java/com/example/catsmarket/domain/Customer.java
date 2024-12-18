package com.example.catsmarket.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Customer {
    Long id;
    UUID customerReference;
    String fullName;
    String username;
    String password;
    String email;
}