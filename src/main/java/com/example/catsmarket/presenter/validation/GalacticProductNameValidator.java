package com.example.catsmarket.presenter.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GalacticProductNameValidator implements ConstraintValidator<ValidGalacticProductName, String> {

    private static final String GALACTIC_PRODUCT_NAME_PATTERN = "(?i).*(galactic|space|nebula|star|cat).*";


    private static final Pattern pattern = Pattern.compile(GALACTIC_PRODUCT_NAME_PATTERN);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.trim().isEmpty()) {
            return false; // Невалідна назва, якщо вона пуста або null
        }

        return pattern.matcher(value).matches();
    }
}
