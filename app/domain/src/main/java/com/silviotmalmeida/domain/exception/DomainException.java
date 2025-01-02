// definição do package
package com.silviotmalmeida.domain.exception;

import com.silviotmalmeida.domain.validation.Error;

import java.util.List;

// classe de exceções do domain
public class DomainException extends NotStackTraceException {

    // atributos
    private final List<Error> errors;

    // construtor privado
    private DomainException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    // factory method para erro único
    public static DomainException with(final Error error) {
        return new DomainException("", List.of(error));
    }

    // factory method para lista de erros
    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    // getters
    public List<Error> getErrors() {
        return errors;
    }
}
