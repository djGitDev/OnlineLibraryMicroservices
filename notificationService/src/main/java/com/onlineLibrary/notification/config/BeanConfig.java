package com.onlineLibrary.notification.config;


import com.fasterxml.jackson.databind.ObjectMapper;
//import com.onlineLibrary.notification.Flux.NotificationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {


//    @Bean
//    public NotificationBuilder notificationBuilder() { return new NotificationBuilder();}

    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }
}

