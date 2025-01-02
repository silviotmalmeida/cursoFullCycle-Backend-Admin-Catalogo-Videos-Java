package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        if (expectedIsActive) Assertions.assertNull(actualCategory.getDeletedAt());
        if (!expectedIsActive) Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategory_thenShouldReturnAnError() {

        // atributos esperados
        final String expectedName = null;
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
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

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategory_thenShouldReturnAnError() {

        // atributos esperados
        final String expectedName = "     ";
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be empty";

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategory_thenShouldReturnAnError() {

        // atributos esperados
        final String expectedName = "ab ";
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategory_thenShouldReturnAnError() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(256, 299));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = new Random().nextBoolean();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivateCategory_thenReturnAInactivatedCategory() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = true;

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        // atributos da classe inicial
        final CategoryID initialId = actualCategory.getId();
        final Instant initialCreateddAt = actualCategory.getCreatedAt();
        final Instant initialUpdatedAt = actualCategory.getUpdatedAt();

        // desativando
        actualCategory.deactivate();

        // executando os testes
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(actualCategory.getId(), initialId);
        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(), initialCreateddAt);
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(initialUpdatedAt));
        Assertions.assertEquals(actualCategory.getUpdatedAt(), actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidActiveCategory_whenCallActivateCategory_thenReturnAActivatedCategory() {

        // atributos esperados
        final String expectedName = Utils.getAlphaNumericString(new Random().nextInt(3, 255));
        final String expectedDescription = Utils.getAlphaNumericString(new Random().nextInt(0, 255));
        final boolean expectedIsActive = false;

        // criando a entidade
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // executando os testes
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), actualCategory.getDeletedAt());

        // atributos da classe inicial
        final CategoryID initialId = actualCategory.getId();
        final Instant initialCreateddAt = actualCategory.getCreatedAt();
        final Instant initialUpdatedAt = actualCategory.getUpdatedAt();

        // ativando
        actualCategory.activate();

        // executando os testes
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(actualCategory.getId(), initialId);
        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(), initialCreateddAt);
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(initialUpdatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
