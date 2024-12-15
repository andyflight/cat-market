package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.PriceService;
import com.example.catsmarket.application.context.recommendation.PriceValidationContext;
import com.example.catsmarket.application.context.recommendation.PriceValidationRequest;
import com.example.catsmarket.application.context.recommendation.PriceValidationResponse;
import com.example.catsmarket.application.exceptions.PriceClientFailedException;
import com.example.catsmarket.application.mapper.PriceValidationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
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
    public PriceValidationContext checkValidation(Double price) {

        log.info("Checking price validation for price {}", price);

        PriceValidationRequest requestDto = priceValidationMapper.toRequest(price);

        PriceValidationResponse responseDto;


        responseDto = restClient.post()
                .uri(priceValidationEndpoint)
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    System.out.println(response.getStatusText());
                    log.error("Price client failed");
                    throw new PriceClientFailedException(response.getStatusText());
                })
                .body(PriceValidationResponse.class);


        return priceValidationMapper.toContext(responseDto);

    }
}
