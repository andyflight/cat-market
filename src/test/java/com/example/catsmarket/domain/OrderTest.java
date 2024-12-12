package com.example.catsmarket.domain;

import com.example.catsmarket.common.OrderStatus;
import com.example.catsmarket.domain.exceptions.EmptyOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DisplayName("Order Model Test")
public class OrderTest {

    private final Order orderWithItems =  Order.builder()
            .orderNumber(UUID.randomUUID())
            .createdAt(new Date())
            .status(OrderStatus.PROCESSING)
            .orderItems(List.of(OrderItem.builder().quantity(2).oldProductPrice(5.00).build()))
            .build();

    private final Order orderWithoutItems =  Order.builder()
            .orderNumber(UUID.randomUUID())
            .createdAt(new Date())
            .status(OrderStatus.PROCESSING)
            .orderItems(List.of())
            .build();


   @Test
    void getTotalPrice_ShouldReturnTotalPrice_WhenOrderItemsExistsAndNotNull() {
        Double expectedTotalPrice = 10.0;
        Double actualTotalPrice = orderWithItems.getTotalPrice();

        assertNotNull(actualTotalPrice);
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }

    @Test
    void getTotalPrice_ShouldThrowException_WhenOrderItemsDoesNotExistOrNull() {
        assertThrows(EmptyOrderException.class, orderWithoutItems::getTotalPrice);
    }
}
