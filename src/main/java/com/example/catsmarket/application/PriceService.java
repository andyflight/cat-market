package com.example.catsmarket.application;

import com.example.catsmarket.application.context.recommendation.PriceValidationContext;

public interface PriceService {

    PriceValidationContext checkValidation(Double price);

}
