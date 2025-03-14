// definição do package
package com.silviotmalmeida.application.category.find;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class FindCategoryUseCaseTest {

    // definindo o usecase que vai receber o mock do repository
    @InjectMocks
    private DefaultFindCategoryUseCase usecase;

    // definindo o mock do repository
    @Mock
    private CategoryGatewayInterface repository;

    // definindo as ações a serem realizadas antes de cada teste
    // função para resetar os mocks
    @BeforeEach
    void cleanUp() {
        Mockito.reset(repository);
    }

    // teste de caminho feliz
    @Test
    public void givenValidInput_whenCallsFindCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final Category expectedCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // criando o input
        final String input = expectedCategory.getId().getValue();

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(expectedCategory.getId()))).thenReturn(Optional.of(expectedCategory.clone()));

        // executando o usecase
        final FindCategoryOutput output = usecase.execute(input);

        // executando os testes
        Assertions.assertInstanceOf(FindCategoryOutput.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCategory.getId().getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedIsActive, output.isActive());
        Assertions.assertEquals(expectedCategory.getCreatedAt(), output.createdAt());
        Assertions.assertEquals(expectedCategory.getUpdatedAt(), output.updatedAt());
        Assertions.assertEquals(expectedCategory.getDeletedAt(), output.deletedAt());

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
    }

    // teste de id inexistente
    @Test
    public void givenNonExistentID_whenCallsFindCategory_shouldReturnException() {

        // atributos esperados
        final CategoryID id = CategoryID.from("123");
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Category id %s not found".formatted(id.getValue());

        // criando o input
        final String input = id.getValue();

        // definindo o comportamento do find (recebe o id e retorna vazio)
        Mockito.when(repository.find(Mockito.eq(id))).thenReturn(Optional.empty());

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
    }

    // teste de erro interno do repository
    @Test
    public void givenValidInput_whenRepositoryThrowsException_shouldReturnException() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final Category expectedCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final String expectedErrorMessage = "Repository error";

        // criando o input
        final String input = expectedCategory.getId().getValue();

        // definindo o comportamento do find (recebe o id e lança exceção interna)
        Mockito.when(repository.find(Mockito.eq(expectedCategory.getId()))).thenThrow(new IllegalStateException(expectedErrorMessage));

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
    }
}
