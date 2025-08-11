package com.onlineLibrary.payment.config;


import com.onlineLibrary.payment.Flux.NotificationBuilderEvent;
import com.onlineLibrary.payment.Persistance.IDBConnection;
import com.onlineLibrary.payment.Persistance.PostgresDBConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public LoaderConfig loaderConfig() {
        return new LoaderConfig();
    }

    @Bean
    public IDBConnection dbConnection(LoaderConfig loaderConfig) {
        return new PostgresDBConnection(loaderConfig);
    }

    @Bean
    public NotificationBuilderEvent notificationBuilder() { return new NotificationBuilderEvent();}
}

