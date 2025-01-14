// definição do package
package com.silviotmalmeida.domain.validation.handler;

import com.silviotmalmeida.domain.validation.Error;
import com.silviotmalmeida.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

// handler que agrupa os erros encontrados antes de lança exceção com a notificação
public class NotificationValidationHandler implements ValidationHandler {

    // atributos
    private final List<Error> errors;

    // construtor
    private NotificationValidationHandler(final List<Error> errors) {
        this.errors = errors;
    }

    // factory method com lista inicial vazia
    public static NotificationValidationHandler create() {
        return new NotificationValidationHandler(new ArrayList<>());
    }

    // factory method com lista inicial com um error
    public static NotificationValidationHandler create(final Error error) {
        return new NotificationValidationHandler(new ArrayList<>()).append(error);
    }

    // insere um erro adicional no handler
    @Override
    public NotificationValidationHandler append(final Error error) {
        this.errors.add(error);
        return this;
    }

    // insere o conteúdo de um handler em outro
    @Override
    public NotificationValidationHandler append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    // retorna a lista de erros contidos no handler
    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
