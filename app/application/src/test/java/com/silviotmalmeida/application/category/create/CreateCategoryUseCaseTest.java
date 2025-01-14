// definição do pacote
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
import com.silviotmalmeida.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    // definindo o usecase que vai receber o mock do repository
    @InjectMocks
    private DefaultCreateCategoryUseCase usecase;

    // definindo o mock do repository
    @Mock
    private CategoryRepositoryInterface repository;

    // teste de caminho feliz
    @Test
    public void givenValidInput_whenCallsCreateCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();

        // criando o input
        final CreateCategoryInput input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do create (recebe qualquer coisa e retorna o primeiro argumento passado ao método)
        Mockito.when(repository.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertInstanceOf(CreateCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.category().getId());
        Assertions.assertEquals(expectedName, output.category().getName());
        Assertions.assertEquals(expectedDescription, output.category().getDescription());
        Assertions.assertEquals(expectedIsActive, output.category().isActive());
        Assertions.assertNotNull(output.category().getCreatedAt());
        Assertions.assertNotNull(output.category().getUpdatedAt());
        if (expectedIsActive) Assertions.assertNull(output.category().getDeletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(output.category().getDeletedAt());

        Assertions.assertNull(notification);

        Mockito.verify(repository, Mockito.times(1)).create(Mockito.any());
    }

    // teste de name inválido
    @Test
    public void givenNullNameInput_whenCallsCreateCategory_thenReturnAnNotification() {

        // atributos esperados
        final String expectedName = null;
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";

        // criando o input
        final CreateCategoryInput input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertNull(output);

        Assertions.assertInstanceOf(NotificationValidationHandler.class, notification);
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(repository, Mockito.times(0)).create(Mockito.any());
    }

    // teste de erro interno do repository
    @Test
    public void givenValidInput_whenRepositoryThrowsException_shouldReturnException() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final String expectedErrorMessage = "Repository error";

        // criando o input
        final CreateCategoryInput input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do create (lançando exceção interna)
        Mockito.when(repository.create(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(repository, Mockito.times(1)).create(Mockito.any());
    }
}
