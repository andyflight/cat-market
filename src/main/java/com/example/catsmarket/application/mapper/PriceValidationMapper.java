package com.example.catsmarket.application.mapper;

import com.example.catsmarket.application.context.price.PriceValidationContext;
import com.example.catsmarket.application.context.price.PriceValidationRequest;
import com.example.catsmarket.application.context.price.PriceValidationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceValidationMapper {

    PriceValidationRequest toRequest(Double price);

    PriceValidationContext toContext(PriceValidationResponse response);

}
