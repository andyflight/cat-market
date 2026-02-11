package com.example.catsmarket.data.db.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name="product_id_seq", sequenceName = "product_id_seq")
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name="code", unique = true, nullable = false)
    private String code;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="price", nullable = false)
    private Double price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="category_product",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    private List<CategoryEntity> categories;

}
