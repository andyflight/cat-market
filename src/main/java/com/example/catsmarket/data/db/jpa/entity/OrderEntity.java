package com.example.catsmarket.data.db.jpa.entity;

import com.example.catsmarket.common.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`order`")
@NamedEntityGraph(
        name = "order-with-items-and-products",
        attributeNodes = {
                @NamedAttributeNode("customer"),
                @NamedAttributeNode(value = "orderItems", subgraph = "orderItems-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "orderItems-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "product")
                        }
                )
        }
)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name="order_id_seq", sequenceName = "order_id_seq")
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name="order_number", nullable = false, unique = true)
    private UUID orderNumber;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private CustomerEntity customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="order_id", nullable = false)
    private List<OrderItemEntity> orderItems;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}
