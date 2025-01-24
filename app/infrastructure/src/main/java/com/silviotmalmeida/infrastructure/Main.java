package com.silviotmalmeida.infrastructure;

import com.silviotmalmeida.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

// declarando como SpringBootApplication
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // utilizando as configurações do profile development
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        // iniciando o Spring
        SpringApplication.run(WebServerConfig.class, args);
    }
}