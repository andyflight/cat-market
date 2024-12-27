package com.example.catsmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CatsMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatsMarketApplication.class, args);
    }

}
