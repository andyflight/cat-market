package com.example.catsmarket.application.mapper;

import com.example.catsmarket.application.context.recommendation.PriceValidationContext;
import com.example.catsmarket.application.context.recommendation.PriceValidationRequest;
import com.example.catsmarket.application.context.recommendation.PriceValidationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceValidationMapper {

    PriceValidationRequest toRequest(Double price);

    PriceValidationContext toContext(PriceValidationResponse response);

}
