package com.example.catsmarket.application;

import com.example.catsmarket.application.context.order.CreateOrderContext;
import com.example.catsmarket.common.OrderStatus;
import com.example.catsmarket.domain.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(CreateOrderContext context);

    Order getOrderByOrderNumber(UUID orderNumber);

    List<Order> getCustomerOrders(UUID customerReference);

    Order updateOrderStatus(UUID orderNumber, OrderStatus orderStatus);

    void deleteOrder(UUID orderNumber);

}
