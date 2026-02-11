package com.example.catsmarket.application;

import com.example.catsmarket.application.context.customer.CustomerContext;
import com.example.catsmarket.domain.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer createCustomer(CustomerContext context);

    Customer updateCustomer(UUID reference, CustomerContext context);

    Customer getByReference(UUID reference);

    void deleteCustomer(UUID reference);

    Customer getCustomerId(UUID reference);

}
