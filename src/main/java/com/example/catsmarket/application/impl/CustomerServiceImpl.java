package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.CustomerService;
import com.example.catsmarket.application.context.customer.CustomerContext;
import com.example.catsmarket.application.exceptions.CustomerNotFoundException;
import com.example.catsmarket.data.CustomerRepository;
import com.example.catsmarket.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer createCustomer(CustomerContext context) {
        Customer customer = Customer.builder()
                .customerReference(UUID.randomUUID())
                .fullName(context.getFullName())
                .username(context.getUsername())
                .password(context.getPassword())
                .email(context.getEmail())
                .build();

        return customerRepository.save(customer);

    }

    @Override
    @Transactional
    public Customer updateCustomer(UUID reference, CustomerContext context) {
        Customer existingCustomer = customerRepository.findByReference(reference)
                .orElseThrow(() -> new CustomerNotFoundException(reference.toString()));

        Customer updatedCustomer = existingCustomer.toBuilder()
                .fullName(context.getFullName())
                .username(context.getUsername())
                .password(context.getPassword())
                .email(context.getEmail())
                .build();


        return customerRepository.save(updatedCustomer);

    }


    @Override
    @Transactional(readOnly = true)
    public Customer getByReference(UUID reference) {
        return customerRepository.findByReference(reference)
                .orElseThrow(() -> new CustomerNotFoundException(reference.toString()));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteCustomer(UUID reference) {

        customerRepository.deleteByReference(reference);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerId(UUID reference) {
        return customerRepository.findCustomerIdProjection(reference)
                .orElseThrow(() -> new CustomerNotFoundException(reference.toString()));
    }
}