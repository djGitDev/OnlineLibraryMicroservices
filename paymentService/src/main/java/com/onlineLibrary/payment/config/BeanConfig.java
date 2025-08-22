package com.onlineLibrary.payment.config;


import com.onlineLibrary.payment.Flux.NotificationBuilderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public LoaderConfig loaderConfig() {
        return new LoaderConfig();
    }

    @Bean
    public NotificationBuilderEvent notificationBuilder() { return new NotificationBuilderEvent();}
}

