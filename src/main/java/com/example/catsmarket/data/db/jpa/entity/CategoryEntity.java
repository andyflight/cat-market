package com.example.catsmarket.data.db.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Data
@NoArgsConstructor
@Entity
@Table(name="category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_seq")
    @SequenceGenerator(name="category_id_seq", sequenceName = "category_id_seq")
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name="name", unique = true, nullable = false)
    private String name;
}
