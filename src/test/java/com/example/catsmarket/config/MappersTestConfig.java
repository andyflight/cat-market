package com.example.catsmarket.config;

import com.example.catsmarket.application.mapper.PriceValidationMapper;
import com.example.catsmarket.presenter.mapper.DtoProductMapper;
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
}
