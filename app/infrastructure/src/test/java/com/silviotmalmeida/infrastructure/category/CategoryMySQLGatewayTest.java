// definicao do package
package com.silviotmalmeida.infrastructure.category;

import com.silviotmalmeida.domain.category.Category;
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
}
