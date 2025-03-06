// definicao do package
package com.silviotmalmeida.application.category.delete;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.exception.DomainException;
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

import java.util.Random;

// utilizando as configurações do profile test-integration, se quiser usar o h2
//@ActiveProfiles("test-integration")
// utilizando as configurações do profile development, se quiser usar o mysql
@ActiveProfiles("development")
// sinalizando para o spring que será um teste
@SpringBootTest(classes = WebServerConfig.class)
public class DeleteCategoryUseCaseIntegrationTest {

    // injetando os usecases
    @Autowired
    private DeleteCategoryUseCase usecase;

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
    public void givenValidInput_whenCallsDeleteCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final Category expectedCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // inserindo a entidade no BD
        repository.saveAndFlush(CategoryJpaModel.from(expectedCategory));

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // criando o input
        final String input = expectedCategory.getId().getValue();

        // executando o usecase
        final Boolean output = usecase.execute(input);

        // executando os testes
        Assertions.assertTrue(output);
        Assertions.assertEquals(0, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(1)).delete(Mockito.any());
    }

    // teste de id inexistente
    @Test
    public void givenNonExistentID_whenCallsDeleteCategory_shouldReturnException() {

        // atributos esperados
        final CategoryID id = CategoryID.from("123");
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Category id %s not found".formatted(id.getValue());

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // criando o input
        final String input = id.getValue();

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(0, repository.count());

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(0)).delete(Mockito.any());
    }

    // teste de erro interno do repository
    @Test
    public void givenValidInput_whenRepositoryThrowsException_shouldReturnException() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);
        final String expectedErrorMessage = "Repository error";

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // inserindo a entidade no BD
        repository.saveAndFlush(CategoryJpaModel.from(initialCategory));

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // criando o input
        final String input = initialCategory.getId().getValue();

        // definindo o comportamento do delete (lançando exceção interna)
        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).delete(Mockito.any());

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(gateway, Mockito.times(1)).delete(Mockito.any());
        Assertions.assertEquals(1, repository.count());
    }
}
