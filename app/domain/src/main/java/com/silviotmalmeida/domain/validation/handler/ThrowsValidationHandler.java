// definição do package
package com.silviotmalmeida.domain.validation.handler;

import com.silviotmalmeida.domain.exception.DomainException;
import com.silviotmalmeida.domain.validation.Error;
import com.silviotmalmeida.domain.validation.ValidationHandler;

import java.util.List;

// handler que lança exceção a cada erro encontrado
public class ThrowsValidationHandler implements ValidationHandler {

    // insere um erro adicional no handler
    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    // insere o conteúdo de um handler em outro
    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    // valida
    @Override
    public ValidationHandler validate(final Validation validation) {
        try {
            validation.validate();
        } catch (Exception exception) {
            throw DomainException.with(new Error(exception.getMessage()));
        }

        return this;
    }

    // retorna a lista de erros contidos no handler
    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
