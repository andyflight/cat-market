package com.example.catsmarket.presenter;

import com.example.catsmarket.AbstractIT;
import com.example.catsmarket.application.OrderService;
import com.example.catsmarket.common.OrderStatus;
import com.example.catsmarket.data.CustomerRepository;
import com.example.catsmarket.data.OrderRepository;
import com.example.catsmarket.data.ProductRepository;
import com.example.catsmarket.domain.*;
import com.example.catsmarket.presenter.dto.order.CreateOrderRequestDto;
import com.example.catsmarket.presenter.dto.order.UpdateOrderStatusRequestDto;
import com.example.catsmarket.presenter.dto.orderItem.OrderItemRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Order Controller IT")
public class OrderControllerIT extends AbstractIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @SpyBean
    private OrderService orderService;


    private final CreateOrderRequestDto validOrderRequest = CreateOrderRequestDto.builder()
            .customerReference("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")
            .orderItems(List.of(
                    OrderItemRequestDto.builder()
                            .productCode("123456789012")
                            .quantity(2)
                            .currentPrice(15.99)
                            .build()
            ))
            .build();

    private final CreateOrderRequestDto validOrderWithMultipleItemsRequest = CreateOrderRequestDto.builder()
            .customerReference("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")
            .orderItems(List.of(
                    OrderItemRequestDto.builder()
                            .productCode("123456789012")
                            .quantity(2)
                            .currentPrice(15.99)
                            .build(),
                    OrderItemRequestDto.builder()
                            .productCode("123456789013")
                            .quantity(10)
                            .currentPrice(35.50)
                            .build()
            ))
            .build();


    private final CreateOrderRequestDto invalidOrderRequest = CreateOrderRequestDto.builder()
            .customerReference("invalid-uuid")
            .orderItems(List.of(
                    OrderItemRequestDto.builder()
                            .productCode("invalid-code")
                            .quantity(-1)
                            .currentPrice(-10.0)
                            .build()
            ))
            .build();

    private final CreateOrderRequestDto emptyOrderRequest = CreateOrderRequestDto.builder()
            .customerReference("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")
            .orderItems(List.of())
            .build();

    private final UpdateOrderStatusRequestDto validUpdateStatusRequest = UpdateOrderStatusRequestDto.builder()
            .orderStatus(OrderStatus.COMPLETED)
            .build();

    @BeforeAll
    static void setupOnce(@Autowired CustomerRepository customerRepository, @Autowired ProductRepository productRepository) {
        customerRepository.save(Customer.builder()
                .id(1L)
                .customerReference(UUID.fromString("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640"))
                .fullName("John Doe")
                .username("johndoe")
                .password("password123")
                .email("john.doe@example.com")
                .build());

        productRepository.save(Product.builder()
                .id(1L)
                .code("123456789012")
                .name("cat food")
                .description("cat food")
                .price(5.00)
                .categories(List.of(Category.builder().id(1L).name("Food").build()))
                .build());

        productRepository.save(Product.builder()
                .id(2L)
                .code("123456789013")
                .name("cat toy")
                .description("cat toy")
                .price(6.00)
                .categories(List.of(Category.builder().id(51L).name("Toys").build()))
                .build());
    }

    @BeforeEach
    void setup() {
        reset(orderService);
        orderRepository.deleteAll();


        orderRepository.save(Order.builder()
                .orderNumber(UUID.fromString("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640"))
                .customer(Customer.builder().id(1L).customerReference(UUID.fromString("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")).build())
                .status(OrderStatus.PROCESSING)
                .createdAt(new Date())
                .orderItems(List.of(
                        OrderItem.builder()
                                .product(Product.builder().id(1L).code("123456789012").build())
                                .quantity(2)
                                .productOldPrice(4.50)
                                .build()
                ))
                .build());
    }


    @Test
    @SneakyThrows
    void createOrder_ShouldCreateOrder() {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").exists())
                .andExpect(jsonPath("$.customerId", is("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")))
                .andExpect(jsonPath("$.orderItems[0].productCode", is("123456789012")))
                .andExpect(jsonPath("$.orderItems[0].quantity", is(2)));
    }

    @Test
    @SneakyThrows
    void createOrder_ShouldThrowException_WhenInputIsInvalid() {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("urn:problem-type:validation-error")))
                .andExpect(jsonPath("$.title", is("Field Validation Exception")));
    }

    @Test
    @SneakyThrows
    void updateOrderStatus_ShouldUpdateStatus_WhenRequestValid() {
        mockMvc.perform(patch("/api/v1/orders/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateStatusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    @SneakyThrows
    void updateOrderStatus_ShouldThrowException_WhenOrderNotFound() {
        mockMvc.perform(patch("/api/v1/orders/00000000-0000-0000-0000-000000000000/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateStatusRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("order-not-found")))
                .andExpect(jsonPath("$.title", is("Order Not Found")));
    }

    @Test
    @SneakyThrows
    void getOrder_ShouldRetrieveOrder_WhenOrderExists() {
        mockMvc.perform(get("/api/v1/orders/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber", is("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")))
                .andExpect(jsonPath("$.status", is("PROCESSING")));
    }

    @Test
    @SneakyThrows
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() {
        mockMvc.perform(delete("/api/v1/orders/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("successful")));
    }

    @Test
    @SneakyThrows
    void deleteOrder_ShouldDoNothing_WhenOrderNotFound() {
        mockMvc.perform(delete("/api/v1/orders/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("successful")));
    }

    @Test
    @SneakyThrows
    void createOrder_ShouldThrowException_WhenOrderEmpty() {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyOrderRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type", is("empty-order")))
                .andExpect(jsonPath("$.title", is("Empty Order")));
    }

    @Test
    @SneakyThrows
    void createOrder_ShouldCreateOrder_WhenRequestValidWithMultipleItems() {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderWithMultipleItemsRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").exists())
                .andExpect(jsonPath("$.customerId", is("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")))
                .andExpect(jsonPath("$.orderItems[*].productCode").exists())
                .andExpect(jsonPath("$.orderItems[*].quantity").exists());
    }

    @Test
    @SneakyThrows
    void getCustomerOrders_ShouldReturnOrder_WhenCustomerExistsAndHaveOrders() {
        mockMvc.perform(get("/api/v1/orders/customer/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")))
                .andExpect(jsonPath("$[0].customerId", is("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")))
                .andExpect(jsonPath("$[0].orderItems[0].productCode", is("123456789012")))
                .andExpect(jsonPath("$[0].orderItems[0].quantity", is(2)));
    }


}


