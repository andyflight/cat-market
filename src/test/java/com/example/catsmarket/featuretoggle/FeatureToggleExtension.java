package com.example.catsmarket.featuretoggle;


import com.example.catsmarket.featuretoggle.annotation.DisableFeature;
import com.example.catsmarket.featuretoggle.annotation.EnableFeature;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FeatureToggleExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        context.getTestMethod().ifPresent(method -> {

            FeatureToggleService featureToggleService = getFeatureToggleService(context);

            if (method.isAnnotationPresent(EnableFeature.class)) {
                String featureName = method.getAnnotation(EnableFeature.class).value().getDescription();
                featureToggleService.enableFeature(featureName);
            }
            else if (method.isAnnotationPresent(DisableFeature.class)) {
                String featureName = method.getAnnotation(DisableFeature.class).value().getDescription();
                featureToggleService.disableFeature(featureName);
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        context.getTestMethod().ifPresent(method -> {

            String featureName = null;

            if (method.isAnnotationPresent(EnableFeature.class)) {
                featureName = method.getAnnotation(EnableFeature.class).value().getDescription();
            }
            else if (method.isAnnotationPresent(DisableFeature.class)) {
                featureName = method.getAnnotation(DisableFeature.class).value().getDescription();
            }

            if (featureName != null) {
                FeatureToggleService featureToggleService = getFeatureToggleService(context);
                if (getFeatureEnabledFlag(context, featureName)) {
                    featureToggleService.enableFeature(featureName);
                }
                else {
                    featureToggleService.disableFeature(featureName);
                }
            }
        });
    }

    private FeatureToggleService getFeatureToggleService(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context).getBean(FeatureToggleService.class);
    }

    private Boolean getFeatureEnabledFlag(ExtensionContext context, String featureName){
        Environment environment = SpringExtension.getApplicationContext(context).getEnvironment();
        return environment.getProperty("application.feature." + featureName + ".enabled", Boolean.class, Boolean.FALSE);
    }
}
