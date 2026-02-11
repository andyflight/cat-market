package com.example.catsmarket.data.db.jpa;

import com.example.catsmarket.data.db.jpa.projection.CustomerIdProjection;
import com.example.catsmarket.data.db.jpa.entity.CustomerEntity;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.Optional;
import java.util.UUID;

@Hidden
public interface JpaCustomerRepository extends NaturalRepository<CustomerEntity, UUID> {

    Optional<CustomerIdProjection> findCustomerByCustomerReference(UUID reference);
}
