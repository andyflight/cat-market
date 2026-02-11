package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.CustomerService;
import com.example.catsmarket.application.OrderService;
import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.application.context.order.CreateOrderContext;
import com.example.catsmarket.application.exceptions.OrderNotFoundException;
import com.example.catsmarket.common.OrderStatus;
import com.example.catsmarket.data.OrderRepository;
import com.example.catsmarket.domain.Order;
import com.example.catsmarket.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    @Override
    @Transactional
    public Order createOrder(CreateOrderContext context) {

        List<OrderItem> orderItems = context.getOrderItems().stream()
                .map(
                        item -> OrderItem.builder()
                                .quantity(item.getQuantity())
                                .product(productService.getProductId(item.getProductCode()))
                                .productOldPrice(item.getCurrentPrice())
                                .build()
                ).toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID())
                .customer(customerService.getCustomerId(context.getCustomerReference()))
                .orderItems(orderItems)
                .status(OrderStatus.PENDING)
                .createdAt(new Date())
                .build();


        return orderRepository.save(order);

    }

    @Override
    @Transactional
    public Order updateOrderStatus(UUID orderNumber, OrderStatus orderStatus) {
        Order oldOrder = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber.toString()));

        Order newOrder = oldOrder.toBuilder()
                .status(orderStatus)
                .build();

        orderRepository.save(newOrder);

        return newOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByOrderNumber(UUID orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getCustomerOrders(UUID customerReference) {
        return orderRepository.findByCustomerReference(customerReference);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteOrder(UUID orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }
}
