package com.example.catsmarket.data.db.jpa.impl;
import com.example.catsmarket.domain.Order;
import com.example.catsmarket.data.OrderRepository;
import com.example.catsmarket.data.db.jpa.JpaOrderRepository;
import com.example.catsmarket.data.db.jpa.mapper.EntityOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository{

    private final JpaOrderRepository jpaOrderRepository;
    private final EntityOrderMapper entityOrderMapper;

    @Override
    public Order save(Order order) {

        return entityOrderMapper.toDomain(
                jpaOrderRepository.save(
                        entityOrderMapper.toEntity(order)
                )
        );
    }

    @Override
    public Optional<Order> findByOrderNumber(UUID orderNumber) {
        return Optional.ofNullable(entityOrderMapper.toDomain(
                jpaOrderRepository.findByNaturalId(orderNumber).orElse(null)
        ));
    }

    @Override
    public List<Order> findByCustomerReference(UUID customerReference) {
        return entityOrderMapper.toDomain(
                jpaOrderRepository.findAllByCustomer_CustomerReference(customerReference)
        );
    }

    @Override
    public void deleteByOrderNumber(UUID orderNumber) {
        jpaOrderRepository.deleteByNaturalId(orderNumber);
    }

    @Override
    public void deleteAll() {
        jpaOrderRepository.deleteAll();
    }
}
