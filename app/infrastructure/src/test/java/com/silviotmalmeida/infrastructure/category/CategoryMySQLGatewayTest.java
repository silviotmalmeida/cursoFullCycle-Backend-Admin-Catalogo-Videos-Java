// definicao do package
package com.silviotmalmeida.infrastructure.category;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategorySearchQuery;
import com.silviotmalmeida.domain.pagination.Pagination;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaModel;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaRepositoryInterface;
import com.silviotmalmeida.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Random;

// utilizando as configurações do profile test-integration, se quiser usar o h2
//@ActiveProfiles("test-integration")
// utilizando as configurações do profile development, se quiser usar o mysql
@ActiveProfiles("development")
// sinalizando para o spring que será um teste (mais pesado)
//@SpringBootTest
// sinalizando para o spring que será um teste apenas do jpa (mais leve)
@DataJpaTest
// sinalizando para o spring outros arquivos que serão necessários para carregamento além do jpa
@ComponentScan(includeFilters = {
        // carregando o gateway
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MySQLGateway]")
})
public class CategoryMySQLGatewayTest {

    // injetando o gateway
    @Autowired
    private CategoryMySQLGateway gateway;

    // injetando o repository
    @Autowired
    private CategoryJpaRepositoryInterface repository;

    // ativando a limpeza do bd antes de cada teste
    @BeforeEach
    void cleanUp() {
        this.repository.deleteAll();
    }

    // testando a injeção de dependências
    @Test
    public void testInjectedDependencies() {
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repository);
    }

    // teste de create
    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();

        // criando a entidade
        final Category expectedCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // confirmando bd vazio
        Assertions.assertEquals(0, repository.count());

        // executando o repository
        final Category categoryBD = gateway.create(expectedCategory);

        // executando os testes
        Assertions.assertEquals(1, repository.count());
        Assertions.assertNotNull(categoryBD);
        Assertions.assertEquals(categoryBD.getId(), expectedCategory.getId());
        Assertions.assertEquals(categoryBD.getName(), expectedCategory.getName());
        Assertions.assertEquals(categoryBD.getDescription(), expectedCategory.getDescription());
        Assertions.assertEquals(categoryBD.isActive(), expectedCategory.isActive());
        Assertions.assertEquals(categoryBD.getCreatedAt(), expectedCategory.getCreatedAt());
        Assertions.assertEquals(categoryBD.getUpdatedAt(), expectedCategory.getUpdatedAt());
        Assertions.assertEquals(categoryBD.getDeletedAt(), expectedCategory.getDeletedAt());
    }

    // teste de update
    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnAUpdatedCategory() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();

        // criando a entidade
        final Category category = Category.newCategory(initialName, initialDescription, initialIsActive);
        final Category initialCategoryBD = repository.saveAndFlush(CategoryJpaModel.from(category)).toAggregate();

        // atributos atualizados
        final String updatedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String updatedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean updateIsActive = !initialIsActive;

        // atualizando a entidade
        category.update(updatedName, updatedDescription, updateIsActive);

        // executando o repository
        final Category updatedCategoryBD = gateway.update(category);

        // executando os testes
        Assertions.assertEquals(1, repository.count());
        Assertions.assertNotNull(updatedCategoryBD);
        Assertions.assertEquals(updatedCategoryBD.getId(), initialCategoryBD.getId());
        Assertions.assertNotEquals(updatedCategoryBD.getName(), initialCategoryBD.getName());
        Assertions.assertNotEquals(updatedCategoryBD.getDescription(), initialCategoryBD.getDescription());
        Assertions.assertNotEquals(updatedCategoryBD.isActive(), initialCategoryBD.isActive());
        Assertions.assertEquals(updatedCategoryBD.getCreatedAt(), initialCategoryBD.getCreatedAt());
        Assertions.assertTrue(updatedCategoryBD.getUpdatedAt().isAfter(initialCategoryBD.getUpdatedAt()));
        if (updateIsActive) Assertions.assertNull(updatedCategoryBD.getDeletedAt());
        if (!updateIsActive) Assertions.assertNotNull(updatedCategoryBD.getDeletedAt());
    }

    // teste de delete com id válido
    @Test
    public void givenAPrePersistedCategory_whenCallsDelete_shouldDeleteCategoryAndReturnTrue() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();

        // criando a entidade
        final Category category = Category.newCategory(initialName, initialDescription, initialIsActive);
        final Category initialCategoryBD = repository.saveAndFlush(CategoryJpaModel.from(category)).toAggregate();

        // executando os testes
        Assertions.assertEquals(1, repository.count());

        // executando o repository
        final boolean sucess = gateway.delete(initialCategoryBD.getId());

        // executando os testes
        Assertions.assertEquals(0, repository.count());
        Assertions.assertTrue(sucess);
    }

    // teste de delete com id inválido
    @Test
    public void givenAInvalidId_whenCallsDelete_shouldReturnFalse() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();

        // criando a entidade, sem persistir no bd
        final Category category = Category.newCategory(initialName, initialDescription, initialIsActive);

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // executando o repository
        final boolean sucess = gateway.delete(category.getId());

        // executando os testes
        Assertions.assertEquals(0, repository.count());
        Assertions.assertFalse(sucess);
    }

    // teste de find com id válido
    @Test
    public void givenAPrePersistedCategory_whenCallsFind_shouldReturnACategory() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();

        // criando a entidade
        final Category category = Category.newCategory(initialName, initialDescription, initialIsActive);
        final Category initialCategoryBD = repository.saveAndFlush(CategoryJpaModel.from(category)).toAggregate();

        // executando o repository
        final Category categoryBD = gateway.find(initialCategoryBD.getId()).orElse(null);

        // executando os testes
        Assertions.assertEquals(1, repository.count());
        Assertions.assertNotNull(categoryBD);
        Assertions.assertEquals(categoryBD.getId(), category.getId());
        Assertions.assertEquals(categoryBD.getName(), category.getName());
        Assertions.assertEquals(categoryBD.getDescription(), category.getDescription());
        Assertions.assertEquals(categoryBD.isActive(), category.isActive());
        Assertions.assertEquals(categoryBD.getCreatedAt(), category.getCreatedAt());
        Assertions.assertEquals(categoryBD.getUpdatedAt(), category.getUpdatedAt());
        Assertions.assertEquals(categoryBD.getDeletedAt(), category.getDeletedAt());
    }

    // teste de find com id inválido
    @Test
    public void givenAInvalidId_whenCallsFind_shouldReturnEmpty() {

        // atributos esperados
        final String initialName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String initialDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean initialIsActive = new Random().nextBoolean();

        // criando a entidade, sem persistir no bd
        final Category category = Category.newCategory(initialName, initialDescription, initialIsActive);

        // executando os testes
        Assertions.assertEquals(0, repository.count());

        // executando o repository
        final Category categoryBD = gateway.find(category.getId()).orElse(null);

        // executando os testes
        Assertions.assertEquals(0, repository.count());
        Assertions.assertNull(categoryBD);
    }

    // teste de paginate com lista existente na página 0
    @Test
    public void givenAPrePersistedCategories_whenCallsPaginatePage0_shouldReturnAPaginationOfCategories() {

        // atributos esperados
        final int expectedCurrentPage = 0;
        final int expectedPerPage = 1;
        final long expectedTotal = 3;
        final Category expectedCategory1 = Category.newCategory(
                "primeiro",
                "",
                new Random().nextBoolean());
        final Category expectedCategory2 = Category.newCategory(
                "segundo",
                "",
                new Random().nextBoolean());
        final Category expectedCategory3 = Category.newCategory(
                "terceiro",
                "",
                new Random().nextBoolean());
        final List<Category> expectedItems = List.of(expectedCategory1, expectedCategory2, expectedCategory3);
        // criando as entidades no BD
        repository.saveAllAndFlush(List.of(
                CategoryJpaModel.from(expectedCategory1),
                CategoryJpaModel.from(expectedCategory2),
                CategoryJpaModel.from(expectedCategory3)
        ));

        // criando a query com sort="asc" por name e sem busca, página 0
        CategorySearchQuery query = new CategorySearchQuery(expectedCurrentPage, expectedPerPage, "", "name", "asc");

        // executando o repository
        Pagination<Category> categories = gateway.paginate(query);

        // executando os testes
        Assertions.assertEquals(expectedCurrentPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.total());
        Assertions.assertEquals(expectedPerPage, categories.items().size());
        Assertions.assertEquals(expectedCategory1.getId(), categories.items().get(0).getId());
    }

    // teste de paginate com lista existente na página 1
    @Test
    public void givenAPrePersistedCategories_whenCallsPaginatePage1_shouldReturnAPaginationOfCategories() {

        // atributos esperados
        final int expectedCurrentPage = 1;
        final int expectedPerPage = 1;
        final long expectedTotal = 3;
        final Category expectedCategory1 = Category.newCategory(
                "primeiro",
                "",
                new Random().nextBoolean());
        final Category expectedCategory2 = Category.newCategory(
                "segundo",
                "",
                new Random().nextBoolean());
        final Category expectedCategory3 = Category.newCategory(
                "terceiro",
                "",
                new Random().nextBoolean());
        final List<Category> expectedItems = List.of(expectedCategory1, expectedCategory2, expectedCategory3);
        // criando as entidades no BD
        repository.saveAllAndFlush(List.of(
                CategoryJpaModel.from(expectedCategory1),
                CategoryJpaModel.from(expectedCategory2),
                CategoryJpaModel.from(expectedCategory3)
        ));

        // criando a query com sort="asc" por name e sem busca, página 1
        CategorySearchQuery query = new CategorySearchQuery(expectedCurrentPage, expectedPerPage, "", "name", "asc");

        // executando o repository
        Pagination<Category> categories = gateway.paginate(query);

        // executando os testes
        Assertions.assertEquals(expectedCurrentPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.total());
        Assertions.assertEquals(expectedPerPage, categories.items().size());
        Assertions.assertEquals(expectedCategory2.getId(), categories.items().get(0).getId());
    }

    // teste de paginate com lista existente filtrada
    @Test
    public void givenAPrePersistedCategories_whenCallsPaginateWithSearchFilters_shouldReturnAPaginationOfCategories() {

        // atributos esperados
        final int expectedCurrentPage = 0;
        final int expectedPerPage = 1;
        final long expectedTotal = 3;
        final Category expectedCategory1 = Category.newCategory(
                "primeiro",
                "",
                new Random().nextBoolean());
        final Category expectedCategory2 = Category.newCategory(
                "segundo",
                "",
                new Random().nextBoolean());
        final Category expectedCategory3 = Category.newCategory(
                "terceiro",
                "",
                new Random().nextBoolean());
        final List<Category> expectedItems = List.of(expectedCategory1, expectedCategory2, expectedCategory3);
        // criando as entidades no BD
        repository.saveAllAndFlush(List.of(
                CategoryJpaModel.from(expectedCategory1),
                CategoryJpaModel.from(expectedCategory2),
                CategoryJpaModel.from(expectedCategory3)
        ));

        // criando a query com terms="eiro" e sort="desc" por name, página 0
        CategorySearchQuery query = new CategorySearchQuery(expectedCurrentPage, expectedPerPage, "eiro", "name", "desc");

        // executando o repository
        Pagination<Category> categories = gateway.paginate(query);

        // executando os testes
        Assertions.assertEquals(expectedCurrentPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(2, categories.total());
        Assertions.assertEquals(expectedPerPage, categories.items().size());
        Assertions.assertEquals(expectedCategory3.getId(), categories.items().get(0).getId());

        // criando a query com terms="undo" e sort="desc" por name, página 0
        query = new CategorySearchQuery(expectedCurrentPage, expectedPerPage, "undo", "name", "desc");

        // executando o repository
        categories = gateway.paginate(query);

        // executando os testes
        Assertions.assertEquals(expectedCurrentPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(1, categories.total());
        Assertions.assertEquals(expectedPerPage, categories.items().size());
        Assertions.assertEquals(expectedCategory2.getId(), categories.items().get(0).getId());

        // criando a query com terms="nenhhum" e sort="desc" por name, página 0
        query = new CategorySearchQuery(expectedCurrentPage, expectedPerPage, "nenhhum", "name", "desc");

        // executando o repository
        categories = gateway.paginate(query);

        // executando os testes
        Assertions.assertEquals(expectedCurrentPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(0, categories.total());
        Assertions.assertEquals(0, categories.items().size());
    }
}
