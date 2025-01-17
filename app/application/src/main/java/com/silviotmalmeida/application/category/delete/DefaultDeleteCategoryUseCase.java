// definição do pacote
package com.silviotmalmeida.application.category.delete;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.Error;

import java.util.Objects;

// implementação do usecase
public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    // atributos
    private final CategoryRepositoryInterface repository;

    // construtor
    public DefaultDeleteCategoryUseCase(final CategoryRepositoryInterface repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // método de execução
    // recebe um input e retorna um output
    @Override
    public Boolean execute(final String input) {

        // inicializando o indicador de sucesso
        boolean success = false;

        // atributos do input
        final CategoryID id = CategoryID.from(input);

        // obtendo a entidade do bd
        Category category = this.repository.find(id)
                .orElseThrow(() -> DomainException.with(new Error("Category id %s not found".formatted(id.getValue()))));
        // deletando
        success = this.repository.delete(category.getId());
        // retornando o output
        return success;
    }
}
