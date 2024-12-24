package com.example.catsmarket.config;

import com.example.catsmarket.data.db.jpa.impl.NaturalRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = "com.example.catsmarket.data.db.jpa",
        repositoryBaseClass = NaturalRepositoryImpl.class
)
public class JpaRepositoryConfig {

}
