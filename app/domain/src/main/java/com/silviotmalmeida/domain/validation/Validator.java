// definição do package
package com.silviotmalmeida.domain.validation;

// classe abstrada a ser estendida pelos validadores
public abstract class Validator {

    // atributos
    private final ValidationHandler handler;

    // construtor
    protected Validator(final ValidationHandler handler) {
        this.handler = handler;
    }

    // método de validação
    public abstract void validate();

    // getters
    protected ValidationHandler getHandler() {
        return handler;
    }
}
