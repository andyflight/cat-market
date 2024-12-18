package com.example.catsmarket.data.db.jpa.impl;

import com.example.catsmarket.data.db.jpa.NaturalRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class NaturalRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements NaturalRepository<T, ID> {

    private final EntityManager entityManager;

    public NaturalRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findByNaturalId(ID naturalId) {
        return entityManager.unwrap(Session.class).bySimpleNaturalId(this.getDomainClass())
                .loadOptional(naturalId);
    }

    @Override
    public List<T> findAllByNaturalId(List<ID> naturalIds) {
        return entityManager.unwrap(Session.class).byMultipleNaturalId(this.getDomainClass())
                .multiLoad(naturalIds);
    }

    @Override
    public void deleteByNaturalId(ID naturalId) {
        findByNaturalId(naturalId).ifPresent(this::delete);
    }
}
