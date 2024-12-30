// definição do package
package com.silviotmalmeida.domain.exception;

import com.silviotmalmeida.domain.validation.Error;

import java.util.List;

// classe de exceções do domain
public class DomainException extends RuntimeException{

    // atributos
    private final List<Error> errors;

    // construtor privado
    private DomainException(final List<Error> errors){
        super("", null, true, false);
        this.errors = errors;
    }

    // método lançador da exceção
    public static DomainException with(final List<Error> errors){
        return new DomainException(errors);
    }

    // getters
    public List<Error> getErrors() {
        return errors;
    }
}
