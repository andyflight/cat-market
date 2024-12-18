package com.example.catsmarket.data.db.jpa;

import com.example.catsmarket.data.db.jpa.entity.OrderEntity;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@Hidden
public interface JpaOrderRepository extends NaturalRepository<OrderEntity, UUID> {

    @EntityGraph(value = "order-with-items-and-products", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT o FROM OrderEntity o WHERE o.customer.customerReference = :customerReference")
    List<OrderEntity> findAllByCustomer_CustomerReference(@Param("customerReference") UUID customerReference);
}
