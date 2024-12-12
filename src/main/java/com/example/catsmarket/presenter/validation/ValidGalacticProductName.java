package com.example.catsmarket.presenter.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = GalacticProductNameValidator.class)
@Documented
public @interface ValidGalacticProductName {
    String GALACTIC_PRODUCT_NAME_SHOULD_BE_VALID = "Invalid Galactic Product Name: The provided name must contain at least one of the following words: 'galactic', 'space', 'nebula', 'star', or 'cat'. Example: 'Galactic Star Cruiser' or 'Nebula Cat Toy'.";

    String message() default GALACTIC_PRODUCT_NAME_SHOULD_BE_VALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
