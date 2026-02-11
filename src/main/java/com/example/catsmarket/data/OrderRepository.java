package com.example.catsmarket.data;

import com.example.catsmarket.domain.Order;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByOrderNumber(UUID orderNumber);

    List<Order> findByCustomerReference(UUID customerReference);

    void deleteByOrderNumber(UUID orderNumber);

    void deleteAll();

}
