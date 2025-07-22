package com.onlineLibrary.orchestre.Config;

import com.google.gson.*;
import feign.codec.Encoder;
import feign.gson.GsonEncoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class GsonConfig {

    // Configuration commune du Gson
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // Format ISO 8601
                .disableHtmlEscaping()
                .create();
    }

    // Configuration serveur (REST)
    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        private final Gson gson;

        public WebConfig(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
            gsonConverter.setGson(gson);
            converters.add(0, gsonConverter); // Ajoute en première position
        }
    }

    @Configuration
    @EnableFeignClients(basePackages = "com.example.MicroservicesClients")
    public static class FeignConfig {
        private final Gson gson;


        public FeignConfig(Gson gson) {
            this.gson = gson;
        }

        @Bean
        public Encoder feignEncoder() {
            return new GsonEncoder(gson); // Utilise l'instance configurée
        }

    }

}