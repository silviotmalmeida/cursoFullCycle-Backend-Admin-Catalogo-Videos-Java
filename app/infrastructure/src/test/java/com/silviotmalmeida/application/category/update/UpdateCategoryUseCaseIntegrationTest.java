// definicao do package
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaModel;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaRepositoryInterface;
import com.silviotmalmeida.infrastructure.configuration.WebServerConfig;
import com.silviotmalmeida.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.temporal.ChronoUnit;
import java.util.Random;

// utilizando as configurações do profile test-integration, se quiser usar o h2
//@ActiveProfiles("test-integration")
// utilizando as configurações do profile development, se quiser usar o mysql
@ActiveProfiles("development")
// sinalizando para o spring que será um teste
@SpringBootTest(classes = WebServerConfig.class)
public class UpdateCategoryUseCaseIntegrationTest {

    // injetando os usecases
    @Autowired
    private UpdateCategoryUseCase usecase;

    // injetando o repository
    @Autowired
    private CategoryJpaRepositoryInterface repository;

    // injetando o gateway para espionar os métodos
    @SpyBean
    private CategoryGatewayInterface gateway;

    // ativando a limpeza do bd antes de cada teste
    @BeforeEach
    void cleanUp() {
        this.repository.deleteAll();
    }

    // testando a injeção de dependências
    @Test
    public void testInjectedDependencies() {
        Assertions.assertNotNull(usecase);
        Assertions.assertNotNull(repository);
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

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // inserindo a entidade no BD
        repository.saveAndFlush(CategoryJpaModel.from(initialCategory));

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId().getValue(), expectedName, expectedDescription, expectedIsActive);

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertInstanceOf(UpdateCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(output.id(), initialCategory.getId().getValue());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedIsActive, output.isActive());
        Assertions.assertNotEquals(initialName, expectedName);
        Assertions.assertNotEquals(initialDescription, expectedDescription);
        Assertions.assertNotEquals(initialIsActive, expectedIsActive);
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertEquals(output.createdAt().truncatedTo(ChronoUnit.MILLIS), initialCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertTrue(output.updatedAt().isAfter(initialCategory.getCreatedAt()));
        Assertions.assertTrue(output.updatedAt().isAfter(initialCategory.getUpdatedAt()));
        if (expectedIsActive) Assertions.assertNull(output.deletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(output.deletedAt());
        Assertions.assertEquals(1, repository.count());

        Assertions.assertNull(notification);

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.any());
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

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // inserindo a entidade no BD
        repository.saveAndFlush(CategoryJpaModel.from(initialCategory));

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId().getValue(), expectedName, expectedDescription, expectedIsActive);

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertInstanceOf(UpdateCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(output.id(), initialCategory.getId().getValue());
        Assertions.assertEquals(output.name(), initialCategory.getName());
        Assertions.assertEquals(output.description(), initialCategory.getDescription());
        Assertions.assertEquals(output.isActive(), initialCategory.isActive());
        Assertions.assertEquals(output.createdAt().truncatedTo(ChronoUnit.MILLIS), initialCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(output.updatedAt().truncatedTo(ChronoUnit.MILLIS), initialCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
        if (!initialIsActive) Assertions.assertEquals(output.deletedAt().truncatedTo(ChronoUnit.MILLIS), initialCategory.getDeletedAt().truncatedTo(ChronoUnit.MILLIS));
        if (initialIsActive) Assertions.assertEquals(output.deletedAt(), initialCategory.getDeletedAt());
        Assertions.assertEquals(1, repository.count());

        Assertions.assertNull(notification);

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.any());
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

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // inserindo a entidade no BD
        repository.saveAndFlush(CategoryJpaModel.from(initialCategory));

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId().getValue(), expectedName, expectedDescription, expectedIsActive);

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
        Assertions.assertEquals(1, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
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

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(id.getValue(), expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(0, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
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

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // inserindo a entidade no BD
        repository.saveAndFlush(CategoryJpaModel.from(initialCategory));

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // criando o input
        final UpdateCategoryInput input = UpdateCategoryInput.with(initialCategory.getId().getValue(), expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do update (lançando exceção interna)
        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).update(Mockito.any());

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(1)).update(Mockito.any());
        Assertions.assertEquals(1, repository.count());
    }
}
