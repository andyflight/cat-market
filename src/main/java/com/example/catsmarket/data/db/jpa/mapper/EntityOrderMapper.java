package com.example.catsmarket.data.db.jpa.mapper;

import com.example.catsmarket.data.db.jpa.entity.OrderEntity;
import com.example.catsmarket.domain.Order;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring", uses = {EntityOrderItemMapper.class, EntityCustomerMapper.class})
public interface EntityOrderMapper {

    Order toDomain(OrderEntity entity);

    List<Order> toDomain(List<OrderEntity> entities);

    OrderEntity toEntity(Order domain);

}
