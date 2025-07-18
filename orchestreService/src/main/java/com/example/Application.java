package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Active la fonctionnalité Feign Client
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}