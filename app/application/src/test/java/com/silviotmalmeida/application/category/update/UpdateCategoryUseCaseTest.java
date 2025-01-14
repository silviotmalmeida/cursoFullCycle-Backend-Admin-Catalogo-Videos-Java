// definição do pacote
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.Category;
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

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    // definindo o usecase que vai receber o mock do repository
    @InjectMocks
    private DefaultUpdateCategoryUseCase usecase;

    // definindo o mock do repository
    @Mock
    private CategoryRepositoryInterface repository;

    // teste de caminho feliz
    @Test
    public void givenValidInput_whenCallsUpdateCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);
        final Instant initialUpdatedAt = initialCategory.getUpdatedAt();
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = !initialIsActive;

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId(), expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do findById (recebe o id e retorna a entidade)
        Mockito.when(repository.findById(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory));
        // definindo o comportamento do update (recebe qualquer coisa e retorna o primeiro argumento passado ao método)
        Mockito.when(repository.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertInstanceOf(UpdateCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.category().getId());
        Assertions.assertEquals(initialCategory.getId(), output.category().getId());
        Assertions.assertEquals(expectedName, output.category().getName());
        Assertions.assertEquals(expectedDescription, output.category().getDescription());
        Assertions.assertEquals(expectedIsActive, output.category().isActive());
        Assertions.assertNotEquals(initialName, expectedName);
        Assertions.assertNotEquals(initialDescription, expectedDescription);
        Assertions.assertNotEquals(initialIsActive, expectedIsActive);
        Assertions.assertNotNull(output.category().getCreatedAt());
        Assertions.assertNotNull(output.category().getUpdatedAt());
        Assertions.assertEquals(initialCategory.getCreatedAt(), output.category().getCreatedAt());
        Assertions.assertTrue(output.category().getUpdatedAt().isAfter(initialCategory.getCreatedAt()));
        Assertions.assertTrue(output.category().getUpdatedAt().isAfter(initialUpdatedAt));
        if (expectedIsActive) Assertions.assertNull(output.category().getDeletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(output.category().getDeletedAt());
        if (!expectedIsActive) Assertions.assertTrue(output.category().getDeletedAt().isAfter(initialCategory.getCreatedAt()));

        Assertions.assertNull(notification);

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(Mockito.any());
    }

    // teste de não atualização devido passagem de atributos nulos
    @Test
    public void givenNullParamInput_whenCallsUpdateCategory_shouldNotUpdate() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);
        final String expectedName = null;
        final String expectedDescription = null;
        final Boolean expectedIsActive = null;

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId(), expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do findById (recebe o id e retorna a entidade)
        Mockito.when(repository.findById(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory));
        // definindo o comportamento do update (recebe qualquer coisa e retorna o primeiro argumento passado ao método)
        Mockito.when(repository.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertInstanceOf(UpdateCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.category().getId());
        Assertions.assertEquals(initialCategory.getId(), output.category().getId());
        Assertions.assertEquals(initialCategory.getName(), output.category().getName());
        Assertions.assertEquals(initialCategory.getDescription(), output.category().getDescription());
        Assertions.assertEquals(initialCategory.isActive(), output.category().isActive());
        Assertions.assertEquals(initialCategory.getCreatedAt(), output.category().getCreatedAt());
        Assertions.assertEquals(initialCategory.getUpdatedAt(), output.category().getUpdatedAt());
        Assertions.assertEquals(initialCategory.getDeletedAt(), output.category().getDeletedAt());

        Assertions.assertNull(notification);

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(Mockito.any());
    }

    // teste de name inválido
    @Test
    public void givenNullNameInput_whenCallsUpdateCategory_thenReturnAnNotification() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);
        final String expectedName = "";
        final String expectedDescription = null;
        final Boolean expectedIsActive = null;
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be empty";

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId(), expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do findById (recebe o id e retorna a entidade)
        Mockito.when(repository.findById(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory));

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertNull(output);

        Assertions.assertInstanceOf(NotificationValidationHandler.class, notification);
        System.out.println(notification.getErrors());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).update(Mockito.any());
    }

    // teste de erro interno do repository
    @Test
    public void givenValidInput_whenRepositoryThrowsException_shouldReturnException() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = !initialIsActive;
        final String expectedErrorMessage = "Repository error";

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId(), expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do findById (recebe o id e retorna a entidade)
        Mockito.when(repository.findById(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory));
        // definindo o comportamento do update (lançando exceção interna)
        Mockito.when(repository.update(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(Mockito.any());
    }
}
