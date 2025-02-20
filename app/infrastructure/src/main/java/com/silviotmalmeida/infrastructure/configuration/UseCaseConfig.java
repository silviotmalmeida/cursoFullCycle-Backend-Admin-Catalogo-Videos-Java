// definição do package
package com.silviotmalmeida.infrastructure.configuration;

import com.silviotmalmeida.application.category.create.CreateCategoryUseCase;
import com.silviotmalmeida.application.category.create.DefaultCreateCategoryUseCase;
import com.silviotmalmeida.application.category.delete.DefaultDeleteCategoryUseCase;
import com.silviotmalmeida.application.category.delete.DeleteCategoryUseCase;
import com.silviotmalmeida.application.category.find.DefaultFindCategoryUseCase;
import com.silviotmalmeida.application.category.find.FindCategoryUseCase;
import com.silviotmalmeida.application.category.paginate.DefaultPaginateCategoryUseCase;
import com.silviotmalmeida.application.category.paginate.PaginateCategoryUseCase;
import com.silviotmalmeida.application.category.update.DefaultUpdateCategoryUseCase;
import com.silviotmalmeida.application.category.update.UpdateCategoryUseCase;
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

    // método para criação dos usecases e injeção das dependências para Category
    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGatewayInterface);
    }
    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGatewayInterface);
    }
    @Bean
    public FindCategoryUseCase findCategoryUseCase(){
        return new DefaultFindCategoryUseCase(categoryGatewayInterface);
    }
    @Bean
    public PaginateCategoryUseCase paginateCategoryUseCase(){
        return new DefaultPaginateCategoryUseCase(categoryGatewayInterface);
    }
    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGatewayInterface);
    }
}
