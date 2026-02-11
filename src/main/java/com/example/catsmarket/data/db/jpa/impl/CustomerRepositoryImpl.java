package com.example.catsmarket.data.db.jpa.impl;

import com.example.catsmarket.data.db.jpa.projection.CustomerIdProjection;
import com.example.catsmarket.data.CustomerRepository;
import com.example.catsmarket.data.db.jpa.JpaCustomerRepository;
import com.example.catsmarket.data.db.jpa.mapper.EntityCustomerMapper;
import com.example.catsmarket.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;
    private final EntityCustomerMapper entityCustomerMapper;

    @Override
    public Customer save(Customer customer) {
        return entityCustomerMapper.toDomain(
                jpaCustomerRepository.save(
                        entityCustomerMapper.toEntity(customer)
                )
        );
    }

    @Override
    public Optional<Customer> findByReference(UUID reference) {
        return Optional.ofNullable(entityCustomerMapper.toDomain(
                jpaCustomerRepository.findByNaturalId(reference).orElse(null)
        ));
    }

    @Override
    public void deleteByReference(UUID reference) {
        jpaCustomerRepository.deleteByNaturalId(reference);

    }

    @Override
    public void deleteAll() {
        jpaCustomerRepository.deleteAll();
    }

    @Override
    public Optional<Customer> findCustomerIdProjection(UUID reference) {
        Optional<CustomerIdProjection> projection = jpaCustomerRepository.findCustomerByCustomerReference(reference);

        return projection.map(proj -> Customer.builder()
                .id(proj.getId())
                .customerReference(reference)
                .build()
        );
    }
}
