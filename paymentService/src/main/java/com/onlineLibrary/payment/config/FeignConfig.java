package com.onlineLibrary.payment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.onlineLibrary.payment.Flux")
public class FeignConfig {

    private final ObjectMapper objectMapper;


    @Autowired
    public FeignConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder(objectMapper);  // Utilise l'ObjectMapper de Spring Boot
    }

}