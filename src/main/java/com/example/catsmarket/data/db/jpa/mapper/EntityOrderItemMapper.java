package com.example.catsmarket.data.db.jpa.mapper;

import com.example.catsmarket.data.db.jpa.entity.OrderItemEntity;
import com.example.catsmarket.domain.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EntityOrderMapper.class})
public interface EntityOrderItemMapper {

    OrderItem toDomain(OrderItemEntity entity);

    List<OrderItem> toDomain(List<OrderItemEntity> entities);

    OrderItemEntity toEntity(OrderItem item);

    List<OrderItemEntity> toEntity(List<OrderItem> items);

}
