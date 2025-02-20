// definição do package
package com.silviotmalmeida.application.category.paginate;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.category.CategorySearchQuery;
import com.silviotmalmeida.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PaginateCategoryUseCaseTest {

    // definindo o usecase que vai receber o mock do repository
    @InjectMocks
    private DefaultPaginateCategoryUseCase usecase;

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
    public void givenValidInput_whenCallsPaginateCategory_shouldReturnValidOutput() {

        // atributos esperados
        final List<Category> categories = List.of(
                Category.newCategory("name1", "description1", true),
                Category.newCategory("name2", "description2", false)
        );
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSortField = "createdAt";
        final String expectedDirection = "asc";
        final int expectedTotal = 2;

        // criando o input
        final CategorySearchQuery input = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSortField, expectedDirection);

        // criando o retorno do repository
        final Pagination<Category> repositoryPagination = new Pagination<>(
                expectedPage, expectedPerPage, categories.size(), categories);

        // definindo o comportamento do Paginate (recebe o input e retorna o pagination)
        Mockito.when(repository.paginate(Mockito.eq(input))).thenReturn(repositoryPagination);

        // executando o usecase
        final Pagination<PaginateCategoryOutput> output = usecase.execute(input);

        // executando os testes
        Assertions.assertInstanceOf(Pagination.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(categories.get(0).getId().getValue(), output.items().get(0).id());
        Assertions.assertEquals(categories.get(0).getName(), output.items().get(0).name());
        Assertions.assertEquals(categories.get(0).getDescription(), output.items().get(0).description());
        Assertions.assertEquals(categories.get(0).isActive(), output.items().get(0).isActive());
        Assertions.assertEquals(categories.get(0).getCreatedAt(), output.items().get(0).createdAt());
        Assertions.assertEquals(categories.get(0).getUpdatedAt(), output.items().get(0).updatedAt());
        Assertions.assertEquals(categories.get(0).getDeletedAt(), output.items().get(0).deletedAt());
        Assertions.assertEquals(categories.get(1).getId().getValue(), output.items().get(1).id());
        Assertions.assertEquals(categories.get(1).getName(), output.items().get(1).name());
        Assertions.assertEquals(categories.get(1).getDescription(), output.items().get(1).description());
        Assertions.assertEquals(categories.get(1).isActive(), output.items().get(1).isActive());
        Assertions.assertEquals(categories.get(1).getCreatedAt(), output.items().get(1).createdAt());
        Assertions.assertEquals(categories.get(1).getUpdatedAt(), output.items().get(1).updatedAt());
        Assertions.assertEquals(categories.get(1).getDeletedAt(), output.items().get(1).deletedAt());
        Mockito.verify(repository, Mockito.times(1)).paginate(Mockito.any());
    }

    // teste de retorno vazio
    @Test
    public void givenValidInput_whenHasNoResults_shouldReturnEmptyOutput() {

        // atributos esperados
        final List<Category> categories = List.<Category>of();
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSortField = "createdAt";
        final String expectedDirection = "asc";
        final int expectedTotal = 0;

        // criando o input
        final CategorySearchQuery input = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSortField, expectedDirection);

        // criando o retorno do repository
        final Pagination<Category> repositoryPagination = new Pagination<>(
                expectedPage, expectedPerPage, categories.size(), categories);

        // definindo o comportamento do Paginate (recebe o input e retorna o pagination)
        Mockito.when(repository.paginate(Mockito.eq(input))).thenReturn(repositoryPagination);

        // executando o usecase
        final Pagination<PaginateCategoryOutput> output = usecase.execute(input);

        // executando os testes
        Assertions.assertInstanceOf(Pagination.class, output);
        Assertions.assertNotNull(output);
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Mockito.verify(repository, Mockito.times(1)).paginate(Mockito.any());
    }

    // teste de erro interno do repository
    @Test
    public void givenValidInput_whenRepositoryThrowsException_shouldReturnException() {

        // atributos esperados
        final List<Category> categories = List.<Category>of();
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSortField = "createdAt";
        final String expectedDirection = "asc";
        final String expectedErrorMessage = "Repository error";

        // criando o input
        final CategorySearchQuery input = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSortField, expectedDirection);

        // definindo o comportamento do Paginate (recebe o input e lança exceção interna)
        Mockito.when(repository.paginate(Mockito.eq(input))).thenThrow(new IllegalStateException(expectedErrorMessage));

        // executando os testes
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> usecase.execute(input));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(repository, Mockito.times(1)).paginate(Mockito.any());
    }
}
