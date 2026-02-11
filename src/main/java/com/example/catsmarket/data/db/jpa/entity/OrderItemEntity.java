package com.example.catsmarket.data.db.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_seq")
    @SequenceGenerator(name="order_item_id_seq", sequenceName = "order_item_id_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name="product_id")
    private ProductEntity product;

    @Column(name = "product_old_price", nullable = false)
    private Double productOldPrice;

    @Column(name="quantity", nullable = false)
    private Integer quantity;

}
