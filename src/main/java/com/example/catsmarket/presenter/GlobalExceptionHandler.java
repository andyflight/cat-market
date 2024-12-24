package com.example.catsmarket.presenter;

import com.example.catsmarket.application.exceptions.*;
import com.example.catsmarket.domain.exceptions.EmptyOrderException;
import com.example.catsmarket.featuretoggle.exception.FeatureDisabledException;
import com.example.catsmarket.presenter.exceptions.ParamsViolationDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

import static com.example.catsmarket.util.ProblemDetailsUtils.getValidationErrorsProblemDetail;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(CategoryPartialResultException.class)
    ProblemDetail handleCategoryPartialResultException(CategoryPartialResultException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("category-partial-result"));
        problemDetail.setTitle("Category Partial Result");
        return problemDetail;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleProductNotFoundException(ProductNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("product-not-found"));
        problemDetail.setTitle("Product Not Found");
        return problemDetail;
    }

    @ExceptionHandler(PriceClientFailedException.class)
    ProblemDetail handlePriceClientFailedValidation(PriceClientFailedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(SERVICE_UNAVAILABLE, ex.getMessage());
        problemDetail.setType(URI.create("price-client-failed"));
        problemDetail.setTitle("External Price Service Unavailable");
        return problemDetail;
    }

    @ExceptionHandler(PriceNotValidException.class)
    ProblemDetail handlePriceNotValidException(PriceNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("price-not-valid"));
        problemDetail.setTitle("Price Validation Failed");
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<ParamsViolationDetails> validationResponse =
                errors.stream().map(err -> ParamsViolationDetails.builder().reason(err.getDefaultMessage()).fieldName(err.getField()).build()).toList();
        return ResponseEntity.status(BAD_REQUEST).body(getValidationErrorsProblemDetail(validationResponse));
    }

    @ExceptionHandler(FeatureDisabledException.class)
    ProblemDetail handleFeatureDisabledException(FeatureDisabledException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("feature-disabled"));
        problemDetail.setTitle("Feature Disabled");
        return problemDetail;
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    ProblemDetail handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("category-not-found"));
        problemDetail.setTitle("Category Not Found");
        return problemDetail;
    }


    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("order-not-found"));
        problemDetail.setTitle("Order Not Found");
        return problemDetail;
    }

    @ExceptionHandler(EmptyOrderException.class)
    ProblemDetail handleEmptyOrderException(EmptyOrderException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create("empty-order"));
        problemDetail.setTitle("Empty Order");
        return problemDetail;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("customer-not-found"));
        problemDetail.setTitle("Customer Not Found");
        return problemDetail;
    }

    @ExceptionHandler(DataAccessException.class)
    ProblemDetail handlePersistenceException(DataAccessException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setType(URI.create("persistence-exception"));
        problemDetail.setTitle("Persistence exception");
        return problemDetail;
    }

}
