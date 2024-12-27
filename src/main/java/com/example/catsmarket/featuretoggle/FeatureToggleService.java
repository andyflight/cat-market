package com.example.catsmarket.featuretoggle;

import com.example.catsmarket.common.Feature;
import com.example.catsmarket.config.FeatureProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class FeatureToggleService {

    private final Map<String, Feature> features;

    public FeatureToggleService(FeatureProperties featureProperties) {
        this.features = new ConcurrentHashMap<>(featureProperties.getFeatures());
    }

    public Boolean isFeatureEnabled(String featureName) {
        Feature feature = features.get(featureName);
        if (feature == null) {
            return false;
        }
        return feature.getEnabled();
    }

    public Double getFeatureValue(String featureName) {
        Feature feature = features.get(featureName);

        if (feature == null) {
            log.warn("Feature {} is not enabled", featureName);
            return 0.0;
        }

        return feature.getValue();
    }

    public void enableFeature(String featureName) {
        features.computeIfPresent(featureName, (k, v) -> {
            v.setEnabled(true);
            return v;
        });

        log.info("Feature {} is now enabled", featureName);
    }

    public void disableFeature(String featureName) {
        features.computeIfPresent(featureName, (k, v) -> {
            v.setEnabled(false);
            return v;
        });

        log.info("Feature {} is now disabled", featureName);
    }

}
