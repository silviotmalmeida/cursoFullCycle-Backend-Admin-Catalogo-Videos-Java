// definição do package
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.application.UseCase;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

// classe abstrata do usecase
// pode retornar um notification ou um output
public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryInput, Either<NotificationValidationHandler, UpdateCategoryOutput>> {
}
