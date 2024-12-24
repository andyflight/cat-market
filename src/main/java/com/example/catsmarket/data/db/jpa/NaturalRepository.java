package com.example.catsmarket.data.db.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface NaturalRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> findByNaturalId(ID naturalId);

    List<T> findAllByNaturalId(List<ID> naturalIds);

    void deleteByNaturalId(ID naturalId);

}
