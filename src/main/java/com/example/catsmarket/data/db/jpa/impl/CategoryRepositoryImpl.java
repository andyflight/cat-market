package com.example.catsmarket.data.db.jpa.impl;

import com.example.catsmarket.data.CategoryRepository;
import com.example.catsmarket.data.db.jpa.JpaCategoryRepository;
import com.example.catsmarket.data.db.jpa.mapper.EntityCategoryMapper;
import com.example.catsmarket.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl  implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final EntityCategoryMapper entityCategoryMapper;

    @Override
    public Optional<Category> findByName(String name) {

        return Optional.ofNullable(entityCategoryMapper.toDomain(
                jpaCategoryRepository.findByNaturalId(name).orElse(null)
        ));
    }

    @Override
    public List<Category> findAllByName(List<String> names) {
        return entityCategoryMapper.toDomain(
                jpaCategoryRepository.findAllByNaturalId(names)
        );
    }

    @Override
    public List<Category> findAll() {
        return entityCategoryMapper.toDomain(
                jpaCategoryRepository.findAll()
        );
    }






}
