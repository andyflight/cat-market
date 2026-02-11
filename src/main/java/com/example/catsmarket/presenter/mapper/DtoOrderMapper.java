package com.example.catsmarket.presenter.mapper;

import com.example.catsmarket.application.context.order.CreateOrderContext;
import com.example.catsmarket.domain.Order;
import com.example.catsmarket.presenter.dto.order.CreateOrderRequestDto;
import com.example.catsmarket.presenter.dto.order.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { DtoOrderItemMapper.class })
public interface DtoOrderMapper {
    CreateOrderContext toCreateContext(CreateOrderRequestDto order);

    @Mapping(source = "customer.customerReference", target = "customerId")
    @Mapping(target="total", expression = "java(order.getTotalPrice())" )
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDto(List<Order> orders);


}