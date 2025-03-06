// definicao do package
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.validation.handler.NotificationValidationHandler;
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

import java.util.Random;

// utilizando as configurações do profile test-integration, se quiser usar o h2
//@ActiveProfiles("test-integration")
// utilizando as configurações do profile development, se quiser usar o mysql
@ActiveProfiles("development")
// sinalizando para o spring que será um teste
@SpringBootTest(classes = WebServerConfig.class)
public class CreateCategoryUseCaseIntegrationTest {

    // injetando os usecases
    @Autowired
    private CreateCategoryUseCase usecase;

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
    public void givenValidInput_whenCallsCreateCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // criando o input
        final CreateCategoryInput input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        // executando o usecase
        final var result = usecase.execute(input);
        final var output = result.getOrNull();
        final var notification = output == null ? result.getLeft() : null;

        // executando os testes
        Assertions.assertEquals(1, repository.count());
        Assertions.assertInstanceOf(CreateCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedIsActive, output.isActive());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        if (expectedIsActive) Assertions.assertNull(output.deletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(output.deletedAt());
        Assertions.assertNull(notification);

        // obtendo a entidade persistida no BD
        final var actualCategory = repository.findById(output.id()).orElse(null);

        // executando os testes
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        if (expectedIsActive) Assertions.assertNull(actualCategory.getDeletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(actualCategory.getDeletedAt());
        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.any());
        Assertions.assertEquals(1, repository.count());
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

        // executando os testes
        Assertions.assertEquals(0, repository.count());

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
        Assertions.assertEquals(0, repository.count());
        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    // teste de erro interno do repository
    @Test
    public void givenValidInput_whenRepositoryThrowsException_shouldReturnException() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final String expectedErrorMessage = "Repository error";

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // criando o input
        final CreateCategoryInput input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        // definindo o comportamento do create (lançando exceção interna)
        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).create(Mockito.any());

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(0, repository.count());
        Mockito.verify(gateway, Mockito.times(1)).create(Mockito.any());
    }
}
