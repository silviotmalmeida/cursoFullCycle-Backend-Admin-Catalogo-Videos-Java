// definição do pacote
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.Error;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

import java.util.Objects;

// implementação do usecase
public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    // atributos
    private final CategoryRepositoryInterface repository;

    // construtor
    public DefaultUpdateCategoryUseCase(final CategoryRepositoryInterface repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // método de execução
    // recebe um input e retorna um notification ou um output
    @Override
    public Either<NotificationValidationHandler, UpdateCategoryOutput> execute(final UpdateCategoryInput input) {

        // atributos do input
        final CategoryID id = CategoryID.from(input.id());
        final String name = input.name();
        final String description = input.description();
        final Boolean isActive = input.isActive();

        // classe de notificação de erros
        final NotificationValidationHandler notification = NotificationValidationHandler.create();

        // obtendo a category do bd
        Category category = this.repository.find(id)
                .orElseThrow(() -> DomainException.with(new Error("Category id %s not found".formatted(id.getValue()))));

        // atualizando a entidade
        category.update(name, description, isActive);
        // validando
        category.validate(notification);

        // se existirem erros, notifica
        if (notification.hasErrors()) {
            return Either.left(notification);
        }
        // senão, prossegue
        else {
            // persistindo
            final Category categoryBD = this.repository.update(category);
            // retornando o output
            return Either.right(UpdateCategoryOutput.from(
                    categoryBD.getId().getValue(),
                    categoryBD.getName(),
                    categoryBD.getDescription(),
                    categoryBD.isActive(),
                    categoryBD.getCreatedAt(),
                    categoryBD.getUpdatedAt(),
                    categoryBD.getDeletedAt()
            ));
        }
    }
}
