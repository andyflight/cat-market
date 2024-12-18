package com.example.catsmarket.presenter.mapper;

import com.example.catsmarket.application.context.order.OrderItemContext;
import com.example.catsmarket.domain.OrderItem;
import com.example.catsmarket.presenter.dto.orderItem.OrderItemRequestDto;
import com.example.catsmarket.presenter.dto.orderItem.OrderItemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoOrderItemMapper {

    OrderItemContext toContext(OrderItemRequestDto orderItem);

    @Mapping(source = "product.code", target = "productCode")
    OrderItemResponseDto toDto(OrderItem orderItem);

}
