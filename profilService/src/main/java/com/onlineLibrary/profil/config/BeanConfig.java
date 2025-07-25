package com.onlineLibrary.profil.config;

import com.onlineLibrary.profil.Persistance.IDBConnection;
import com.onlineLibrary.profil.Persistance.PostgresDBConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public IDBConnection dbConnection() {
            return new PostgresDBConnection();
        }
}

