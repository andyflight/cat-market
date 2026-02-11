package com.example.catsmarket.featuretoggle.aspect;

import com.example.catsmarket.common.FeatureName;
import com.example.catsmarket.featuretoggle.FeatureToggleService;
import com.example.catsmarket.featuretoggle.annotation.FeatureToggle;
import com.example.catsmarket.featuretoggle.exception.FeatureDisabledException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Around("@annotation(featureToggle)")
    public Object checkFeatureEnabled(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        FeatureName featureName = featureToggle.value();

        if (!featureToggleService.isFeatureEnabled(featureName.getDescription())) {

            if (featureToggle.throwExceptionOnDisabled()) {
                log.error("Feature {} is not enabled. Please enable it before proceeding. ", featureName.getDescription());
                throw new FeatureDisabledException(featureName.getDescription());
            }

            log.warn("Feature {} is not enabled. Returning null for method {}", featureName.getDescription(), joinPoint.getSignature().getName());
            return null;
        }

        log.info("Feature {} is enabled. Proceeding with method {}", featureName.getDescription(), joinPoint.getSignature().getName());
        return joinPoint.proceed();
    }
}
