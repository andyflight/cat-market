package com.example.catsmarket.data.db.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name="customer_id_seq", sequenceName = "customer_id_seq")
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name="customer_reference", unique = true, nullable = false)
    private UUID customerReference;

    @Column(name="fullname", nullable = false)
    private String fullName;

    @Column(name="username", unique = true, nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="email", nullable = false)
    private String email;
}
