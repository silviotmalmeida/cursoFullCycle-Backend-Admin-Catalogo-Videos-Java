// definição do package
package com.silviotmalmeida.application.category.find;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.Error;

import java.util.Objects;

// implementação do usecase
public class DefaultFindCategoryUseCase extends FindCategoryUseCase {

    // atributos
    private final CategoryGatewayInterface repository;

    // construtor
    public DefaultFindCategoryUseCase(final CategoryGatewayInterface repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // método de execução
    // recebe um input e retorna um output
    @Override
    public FindCategoryOutput execute(final String input) {

        // inicializando o indicador de sucesso
        boolean success = false;

        // atributos do input
        final CategoryID id = CategoryID.from(input);

        // obtendo a entidade do bd
        Category category = this.repository.find(id)
                .orElseThrow(() -> DomainException.with(new Error("Category id %s not found".formatted(id.getValue()))));
        // retornando o output
        return FindCategoryOutput.from(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}
