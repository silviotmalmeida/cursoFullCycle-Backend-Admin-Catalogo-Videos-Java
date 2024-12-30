package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {

        // atributos esperados
        final String expectedName = "name";
        final String expectedDescription = "description";
        final boolean expectedIsActive = new Random().nextBoolean();

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategory_thenShouldReturnAnError() {

        // atributos esperados
        final String expectedName = null;
        final String expectedDescription = "description";
        final boolean expectedIsActive = new Random().nextBoolean();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
