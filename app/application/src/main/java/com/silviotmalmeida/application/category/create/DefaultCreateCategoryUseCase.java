// definição do pacote
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

// implementação do usecase
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    // atributos
    private final CategoryRepositoryInterface repository;

    // construtor
    public DefaultCreateCategoryUseCase(final CategoryRepositoryInterface repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // método de execução
    // recebe um input e retorna um output
    @Override
    public CreateCategoryOutput execute(final CreateCategoryInput input) {

        // atributos do input
        final String name = input.name();
        final String description = input.description();
        final boolean isActive = input.isActive();

        // criando a entidade
        final Category category = Category.newCategory(name, description, isActive);
        // validando
        category.validate(new ThrowsValidationHandler());
        // persistindo
        final Category categoryBD = this.repository.create(category);

        // retornando o output
        return CreateCategoryOutput.from(categoryBD);
    }
}
