// definição do pacote
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.utils.Utils;
import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.util.Random;

public class CreateCategoryUseCaseTest {

    // teste de caminho feliz
    @Test
    public void givenValidInput_whenCallsCreateCategory_shouldReturnValidOutput(){

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();

        // criando o input
        final CreateCategoryInput input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        // criando o mock do repository
        final CategoryRepositoryInterface repository = Mockito.mock(CategoryRepositoryInterface.class);
        Mockito.when(repository.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // criando o usecase
        final CreateCategoryUseCase usecase = new DefaultCreateCategoryUseCase(repository);
        final CreateCategoryOutput output = usecase.execute(input);

        // executando os testes
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.category().getId());
        Assertions.assertEquals(expectedName, output.category().getName());
        Assertions.assertEquals(expectedDescription, output.category().getDescription());
        Assertions.assertEquals(expectedIsActive, output.category().isActive());
        Assertions.assertNotNull(output.category().getCreatedAt());
        Assertions.assertNotNull(output.category().getUpdatedAt());
        if (expectedIsActive) Assertions.assertNull(output.category().getDeletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(output.category().getDeletedAt());

        Mockito.verify(repository, Mockito.times(1)).create(Mockito.any());
    }
}
