package com.onlineLibrary.order.config;


import com.onlineLibrary.order.Flux.Implementations.SynchronizedOrderManager;
import com.onlineLibrary.order.Flux.Interfaces.ISynchronizedOrderManager;
import com.onlineLibrary.order.Persistance.Implementations.PostgresDBConnection;
import com.onlineLibrary.order.Persistance.Interfaces.IDBConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public IDBConnection dbConnection() {
            return new PostgresDBConnection();
        }

    @Bean
    public ISynchronizedOrderManager synchronizedOrderManager() {
        return new SynchronizedOrderManager();
    }
}

