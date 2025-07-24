package com.onlineLibrary.cart.config;


import com.onlineLibrary.cart.Persistance.Implementations.PostgresDBConnection;
import com.onlineLibrary.cart.Persistance.Interfaces.IDBConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public IDBConnection dbConnection() {
            return new PostgresDBConnection();
        }

}

