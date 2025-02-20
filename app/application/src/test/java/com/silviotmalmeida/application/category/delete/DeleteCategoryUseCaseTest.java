// definição do package
package com.silviotmalmeida.application.category.delete;

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
public class DeleteCategoryUseCaseTest {

    // definindo o usecase que vai receber o mock do repository
    @InjectMocks
    private DefaultDeleteCategoryUseCase usecase;

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
    public void givenValidInput_whenCallsDeleteCategory_shouldReturnValidOutput() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();
        final Category initialCategory = Category.newCategory(initialName, initialDescription, initialIsActive);

        // criando o input
        final String input = initialCategory.getId().getValue();

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory.clone()));
        // definindo o comportamento do delete (recebe o id e retorna true)
        Mockito.when(repository.delete(Mockito.eq(initialCategory.getId()))).thenReturn(true);

        // executando o usecase
        final Boolean output = usecase.execute(input);

        // executando os testes
        Assertions.assertTrue(output);

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }

    // teste de id inexistente
    @Test
    public void givenNonExistentID_whenCallsDeleteCategory_shouldReturnException() {

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
        Mockito.verify(repository, Mockito.times(0)).delete(Mockito.any());
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

        // criando o input
        final String input = initialCategory.getId().getValue();

        // definindo o comportamento do find (recebe o id e retorna a entidade clonada)
        Mockito.when(repository.find(Mockito.eq(initialCategory.getId()))).thenReturn(Optional.of(initialCategory.clone()));
        // definindo o comportamento do delete (lançando exceção interna)
        Mockito.when(repository.delete(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(repository, Mockito.times(1)).find(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any());
    }
}
