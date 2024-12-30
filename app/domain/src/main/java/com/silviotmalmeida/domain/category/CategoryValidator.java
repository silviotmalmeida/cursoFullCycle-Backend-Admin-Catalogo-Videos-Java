// definição do package
package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.validation.Error;
import com.silviotmalmeida.domain.validation.ValidationHandler;
import com.silviotmalmeida.domain.validation.Validator;

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

        // name não pode ser nulo
        if (this.category.getName() == null) {
            this.getHandler().append(new Error("'name' should not be null"));
        }

    }
}
