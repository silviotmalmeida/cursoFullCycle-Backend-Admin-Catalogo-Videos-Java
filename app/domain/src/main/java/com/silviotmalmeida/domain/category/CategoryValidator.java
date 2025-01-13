// definição do package
package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.validation.Error;
import com.silviotmalmeida.domain.validation.ValidationHandler;
import com.silviotmalmeida.domain.validation.Validator;

import java.util.Objects;

// classe de validação da entidade
public class CategoryValidator extends Validator {

    // atributos
    private final Category category;

    // construtor
    public CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    // método de validação
    @Override
    public void validate() {

        validateName();
    }

    // método de validação de name
    private void validateName() {

        // name não pode ser nulo
        if (this.category.getName() == null) {
            this.getHandler().append(new Error("'name' should not be null"));
        } else {

            // name não pode ser vazio
            if (this.category.getName().isBlank()) {
                this.getHandler().append(new Error("'name' should not be empty"));
            }

            // name não pode ser menor que 3, removendo os espaços em branco
            if (this.category.getName().trim().length() < 3) {
                this.getHandler().append(new Error("'name' must be between 3 and 255 characters"));
            }

            // name não pode ser maior que 255
            if (this.category.getName().length() > 255) {
                this.getHandler().append(new Error("'name' must be between 3 and 255 characters"));
            }
        }
    }
}
