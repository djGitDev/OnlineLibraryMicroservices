package com.onlineLibrary.inventary.config;



import com.onlineLibrary.inventary.Persistance.IDBConnection;
import com.onlineLibrary.inventary.Persistance.Impl.PostgresDBConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public IDBConnection dbConnection() {
            return new PostgresDBConnection();
        }

}

