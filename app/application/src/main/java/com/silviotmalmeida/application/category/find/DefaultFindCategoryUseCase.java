// definição do pacote
package com.silviotmalmeida.application.category.find;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.Error;

import java.util.Objects;

// implementação do usecase
public class DefaultFindCategoryUseCase extends FindCategoryUseCase {

    // atributos
    private final CategoryRepositoryInterface repository;

    // construtor
    public DefaultFindCategoryUseCase(final CategoryRepositoryInterface repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // método de execução
    // recebe um input e retorna um output
    @Override
    public FindCategoryOutput execute(final FindCategoryInput input) {

        // inicializando o indicador de sucesso
        boolean success = false;

        // atributos do input
        final CategoryID id = input.id();

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
