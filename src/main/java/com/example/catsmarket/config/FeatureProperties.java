package com.example.catsmarket.config;

import com.example.catsmarket.common.Feature;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Configuration
@ConfigurationProperties(prefix="application")
public class FeatureProperties {

    private ConcurrentHashMap<String, Feature> features;

}
