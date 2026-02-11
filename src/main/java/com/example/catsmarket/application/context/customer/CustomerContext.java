package com.example.catsmarket.application.context.customer;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CustomerContext {

    String fullName;

    String username;

    String password;

    String email;
}
