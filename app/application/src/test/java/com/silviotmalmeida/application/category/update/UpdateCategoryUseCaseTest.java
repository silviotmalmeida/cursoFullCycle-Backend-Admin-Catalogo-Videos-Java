// definição do pacote
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
import com.silviotmalmeida.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    // definindo as ações a serem realizadas antes de cada teste
    // função para resetar os mocks
    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

    // teste de caminho feliz
    @Test
    public void givenValidInput_whenCallsUpdateCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = !initialIsActive;

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId(), expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory.clone()));
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
        Assertions.assertEquals(output.category().getId(), initialCategory.getId());
        Assertions.assertEquals(expectedName, output.category().getName());
        Assertions.assertEquals(expectedDescription, output.category().getDescription());
        Assertions.assertEquals(expectedIsActive, output.category().isActive());
        Assertions.assertNotEquals(initialName, expectedName);
        Assertions.assertNotEquals(initialDescription, expectedDescription);
        Assertions.assertNotEquals(initialIsActive, expectedIsActive);
        Assertions.assertNotNull(output.category().getCreatedAt());
        Assertions.assertNotNull(output.category().getUpdatedAt());
        Assertions.assertEquals(output.category().getCreatedAt(), initialCategory.getCreatedAt());
        Assertions.assertTrue(output.category().getUpdatedAt().isAfter(initialCategory.getCreatedAt()));
        Assertions.assertTrue(output.category().getUpdatedAt().isAfter(initialCategory.getUpdatedAt()));
        if (expectedIsActive) Assertions.assertNull(output.category().getDeletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(output.category().getDeletedAt());

        Assertions.assertNull(notification);

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
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

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory.clone()));
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
        Assertions.assertEquals(output.category().getId(), initialCategory.getId());
        Assertions.assertEquals(output.category().getName(), initialCategory.getName());
        Assertions.assertEquals(output.category().getDescription(), initialCategory.getDescription());
        Assertions.assertEquals(output.category().isActive(), initialCategory.isActive());
        Assertions.assertEquals(output.category().getCreatedAt(), initialCategory.getCreatedAt());
        Assertions.assertEquals(output.category().getUpdatedAt(), initialCategory.getUpdatedAt());
        Assertions.assertEquals(output.category().getDeletedAt(), initialCategory.getDeletedAt());

        Assertions.assertNull(notification);

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
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

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory.clone()));

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

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).update(Mockito.any());
    }

    // teste de id inexistente
    @Test
    public void givenNonExistentID_whenCallsUpdateCategory_shouldReturnException() {

        // atributos esperados
        final CategoryID id = CategoryID.from("123");
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Category id %s not found".formatted(id.getValue());

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(id, expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do find (recebe o id e retorna vazio)
        Mockito.when(repository.find(Mockito.eq(id))).thenReturn(Optional.empty());

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
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

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory.clone()));
        // definindo o comportamento do update (lançando exceção interna)
        Mockito.when(repository.update(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).update(Mockito.any());
    }
}
