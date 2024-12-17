package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.PriceService;
import com.example.catsmarket.application.context.recommendation.PriceValidationContext;
import com.example.catsmarket.application.context.recommendation.PriceValidationRequest;
import com.example.catsmarket.application.context.recommendation.PriceValidationResponse;
import com.example.catsmarket.application.exceptions.PriceClientFailedException;
import com.example.catsmarket.application.mapper.PriceValidationMapper;
import com.example.catsmarket.common.FeatureName;
import com.example.catsmarket.featuretoggle.annotation.FeatureToggle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PriceServiceImpl implements PriceService {

    private final RestClient restClient;
    private final String priceValidationEndpoint;
    private final PriceValidationMapper priceValidationMapper;

    public PriceServiceImpl(
            @Qualifier("priceValidationRestClient") RestClient restClient,
            @Value("${application.price-service.price}") String priceValidationEndpoint,
            PriceValidationMapper priceValidationMapper
            ) {
        this.restClient = restClient;
        this.priceValidationEndpoint = priceValidationEndpoint;
        this.priceValidationMapper = priceValidationMapper ;

    }

    @Override
    @FeatureToggle(value = FeatureName.PRICE_VALIDATION, throwExceptionOnDisabled = false)
    public PriceValidationContext checkValidation(Double price) {
        PriceValidationRequest requestDto = priceValidationMapper.toRequest(price);

        PriceValidationResponse responseDto;


        responseDto = restClient.post()
                .uri(priceValidationEndpoint)
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    System.out.println(response.getStatusText());
                    throw new PriceClientFailedException(response.getStatusText());
                })
                .body(PriceValidationResponse.class);


        return priceValidationMapper.toContext(responseDto);

    }
}
