package com.silviotmalmeida.infrastructure;

import com.silviotmalmeida.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// declarando como SpringBootApplication
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // iniciando o Spring
        SpringApplication.run(WebServerConfig.class, args);
    }
}