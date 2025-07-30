package com.onlineLibrary.payment.config;


import com.onlineLibrary.payment.Flux.NotificationBuilder;
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
    public NotificationBuilder notificationBuilder() { return new NotificationBuilder();}
}

