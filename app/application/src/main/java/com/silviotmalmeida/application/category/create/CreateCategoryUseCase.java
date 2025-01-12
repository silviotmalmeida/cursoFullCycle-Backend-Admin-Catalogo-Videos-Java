// definição do pacote
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.application.UseCase;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

// classe abstrata do usecase
// pode retornar um notification ou um output
public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryInput, Either<NotificationValidationHandler, CreateCategoryOutput>> {
}
