package com.onlineLibrary.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineLibrary.order.Flux.Implementations.SynchronizedOrderManager;
import com.onlineLibrary.order.Flux.Interfaces.ISynchronizedOrderManager;
import com.onlineLibrary.order.Persistance.Implementations.PostgresDBConnection;
import com.onlineLibrary.order.Persistance.Interfaces.IDBConnection;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.onlineLibrary.order.Flux.Interfaces")
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