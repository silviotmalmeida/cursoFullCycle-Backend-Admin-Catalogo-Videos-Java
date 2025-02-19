// definição do package
package com.silviotmalmeida.infrastructure.configuration;

import com.silviotmalmeida.application.category.create.CreateCategoryUseCase;
import com.silviotmalmeida.application.category.create.DefaultCreateCategoryUseCase;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// classe de configuração do Spring, responsável por injetar os gateways aos usecases
@Configuration
public class UseCaseConfig {

    // gateways
    private final CategoryGatewayInterface categoryGatewayInterface;

    // construtor
    public UseCaseConfig(final CategoryGatewayInterface categoryGatewayInterface){
        this.categoryGatewayInterface = categoryGatewayInterface;
    }

    // método para criação do CreateCategoryUseCase e injeção das dependências
    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGatewayInterface);
    }
}
