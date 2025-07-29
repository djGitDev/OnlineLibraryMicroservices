package com.onlineLibrary.profil.config;

import com.onlineLibrary.profil.Persistance.IDBConnection;
import com.onlineLibrary.profil.Persistance.PostgresDBConnection;
import org.springframework.beans.factory.annotation.Autowired;
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

}

