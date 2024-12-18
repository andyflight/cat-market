package com.example.catsmarket.data;

import com.example.catsmarket.AbstractIT;
import com.example.catsmarket.common.OrderStatus;
import com.example.catsmarket.data.db.jpa.JpaOrderRepository;
import com.example.catsmarket.data.db.jpa.entity.CustomerEntity;
import com.example.catsmarket.data.db.jpa.entity.OrderEntity;
import com.example.catsmarket.data.db.jpa.entity.OrderItemEntity;
import com.example.catsmarket.data.db.jpa.entity.ProductEntity;
import com.example.catsmarket.domain.*;
import com.example.catsmarket.domain.Order;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ImportAutoConfiguration
@DisplayName("Order Repository IT")
@Transactional
public class OrderRepositoryIT extends AbstractIT {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    private final UUID testCustomerReference = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private final UUID testOrderNumber = UUID.fromString("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640");

    @BeforeAll
    static void setupOnce(@Autowired CustomerRepository customerRepository, @Autowired ProductRepository productRepository) {
        customerRepository.save(Customer.builder()
                .id(1L)
                .customerReference(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
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
        jpaOrderRepository.deleteAll();

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setCustomerReference(testCustomerReference);
        customerEntity.setFullName("John Doe");
        customerEntity.setUsername("johndoe");
        customerEntity.setPassword("password123");
        customerEntity.setEmail("john.doe@example.com");

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setCode("123456789012");
        productEntity.setName("Cat Food");
        productEntity.setDescription("Premium cat food");
        productEntity.setPrice(5.00);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProduct(productEntity);
        orderItemEntity.setQuantity(2);
        orderItemEntity.setProductOldPrice(4.50);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNumber(testOrderNumber);
        orderEntity.setCustomer(customerEntity);
        orderEntity.setStatus(OrderStatus.PROCESSING);
        orderEntity.setOrderItems(List.of(orderItemEntity));
        orderEntity.setCreatedAt(new Date());

        jpaOrderRepository.save(orderEntity);
    }

    @Test
    void saveOrder_ShouldPersistOrder() {
        Order testOrder = Order.builder()
                .orderNumber(UUID.randomUUID())
                .customer(Customer.builder().id(1L).customerReference(testCustomerReference).build())
                .status(OrderStatus.PENDING)
                .createdAt(new Date())
                .orderItems(List.of(
                        OrderItem.builder()
                                .product(Product.builder().code("123456789012").build())
                                .quantity(1)
                                .productOldPrice(5.00)
                                .build()
                ))
                .build();

        Order savedOrder = orderRepository.save(testOrder);

        Assertions.assertNotNull(savedOrder.getOrderNumber());
        Assertions.assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        Assertions.assertEquals(1, savedOrder.getOrderItems().size());
        Assertions.assertEquals("123456789012", savedOrder.getOrderItems().getFirst().getProduct().getCode());
    }

    @Test
    void findByOrderNumber_ShouldRetrieveOrder() {
        Optional<Order> order = orderRepository.findByOrderNumber(testOrderNumber);

        Assertions.assertTrue(order.isPresent());
        Assertions.assertEquals(testOrderNumber, order.get().getOrderNumber());
        Assertions.assertEquals(OrderStatus.PROCESSING, order.get().getStatus());
        Assertions.assertEquals(2, order.get().getOrderItems().getFirst().getQuantity());
    }

    @Test
    void findByCustomerReference_ShouldRetrieveCustomerOrders() {
        List<Order> orders = orderRepository.findByCustomerReference(testCustomerReference);

        Assertions.assertFalse(orders.isEmpty());
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(testOrderNumber, orders.getFirst().getOrderNumber());
    }

    @Test
    void deleteByOrderNumber_ShouldDeleteOrder() {
        orderRepository.deleteByOrderNumber(testOrderNumber);

        Optional<Order> order = orderRepository.findByOrderNumber(testOrderNumber);
        Assertions.assertTrue(order.isEmpty());
    }

    @Test
    void deleteAll_ShouldClearAllOrders() {
        orderRepository.deleteAll();

        List<Order> orders = orderRepository.findByCustomerReference(testCustomerReference);
        Assertions.assertTrue(orders.isEmpty());
    }




}
