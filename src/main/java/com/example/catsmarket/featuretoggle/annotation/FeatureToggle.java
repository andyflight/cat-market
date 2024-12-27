package com.example.catsmarket.featuretoggle.annotation;

import com.example.catsmarket.common.FeatureName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FeatureToggle {

    FeatureName value();

    boolean throwExceptionOnDisabled() default true;
}