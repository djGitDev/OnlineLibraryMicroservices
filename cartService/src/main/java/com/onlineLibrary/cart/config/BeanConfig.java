package com.onlineLibrary.cart.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {


    @Bean
    public LoaderConfig loaderConfig() {
        return new LoaderConfig();
    }

}

