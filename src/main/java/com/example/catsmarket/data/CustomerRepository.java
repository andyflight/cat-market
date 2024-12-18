package com.example.catsmarket.data;

import com.example.catsmarket.domain.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findByReference(UUID reference);

    Optional<Customer> findCustomerIdProjection(UUID reference);

    void deleteByReference(UUID reference);

    void deleteAll();
}
