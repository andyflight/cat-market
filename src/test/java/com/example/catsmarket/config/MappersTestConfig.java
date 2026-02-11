package com.example.catsmarket.config;

import com.example.catsmarket.application.mapper.PriceValidationMapper;
import com.example.catsmarket.data.db.jpa.mapper.*;
import com.example.catsmarket.presenter.mapper.*;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersTestConfig {

    @Bean
    public PriceValidationMapper priceValidationMapper() {
        return Mappers.getMapper(PriceValidationMapper.class);
    }

    @Bean
    public DtoProductMapper dtoProductMapper() {
        return Mappers.getMapper(DtoProductMapper.class);
    }

    @Bean
    public DtoCategoryMapper dtoCategoryMapper() {
        return Mappers.getMapper(DtoCategoryMapper.class);
    }

    @Bean
    public DtoOrderMapper dtoOrderMapper() {
        return Mappers.getMapper(DtoOrderMapper.class);
    }

    @Bean
    public DtoOrderItemMapper dtoOrderItemMapper() {
        return Mappers.getMapper(DtoOrderItemMapper.class);
    }

    @Bean
    public DtoCustomerMapper dtoCustomerMapper() {
        return Mappers.getMapper(DtoCustomerMapper.class);
    }

    @Bean
    public EntityCustomerMapper entityCustomerMapper() {
        return Mappers.getMapper(EntityCustomerMapper.class);
    }

    @Bean
    public EntityProductMapper entityProductMapper() {
        return Mappers.getMapper(EntityProductMapper.class);
    }

    @Bean
    public EntityCategoryMapper entityCategoryMapper() {
        return Mappers.getMapper(EntityCategoryMapper.class);
    }

    @Bean
    public EntityOrderMapper entityOrderMapper() {
        return Mappers.getMapper(EntityOrderMapper.class);
    }

    @Bean
    public EntityOrderItemMapper entityOrderItemMapper() {
        return Mappers.getMapper(EntityOrderItemMapper.class);
    }

}
