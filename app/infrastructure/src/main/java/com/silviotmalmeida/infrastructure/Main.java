package com.silviotmalmeida.infrastructure;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaModel;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaRepositoryInterface;
import com.silviotmalmeida.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

// declarando como SpringBootApplication
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // utilizando as configurações do profile development
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        // iniciando o Spring
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    public ApplicationRunner runner(CategoryJpaRepositoryInterface repository){
        return args -> {
            List<CategoryJpaModel> all1 = repository.findAll();
            System.out.println(all1);

            Category filmes = Category.newCategory("Filmes", null, true);
            System.out.println(filmes);

            CategoryJpaModel model = CategoryJpaModel.from(filmes);
            System.out.println(model);

            repository.saveAndFlush(model);

            List<CategoryJpaModel> all2 = repository.findAll();
            System.out.println(all2);

            repository.deleteAll();
        };
    }
}