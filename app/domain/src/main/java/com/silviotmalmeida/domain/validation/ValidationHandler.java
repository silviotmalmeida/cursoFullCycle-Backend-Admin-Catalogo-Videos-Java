// definição do package
package com.silviotmalmeida.domain.validation;

import java.util.List;

// interface para gerenciar as validações
public interface ValidationHandler {

    // insere um erro adicional no handler
    ValidationHandler append(Error error);

    // insere o conteúdo de um handler em outro
    ValidationHandler append(ValidationHandler handler);

    // valida
    ValidationHandler validate(Validation validation);

    // retorna a lista de erros contidos no handler
    List<Error> getErrors();

    // método default (implementado na interface) que informa a existência de erros contidos no handler
    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    // interface de execução da validação, através de lambda
    public interface Validation {
        void validate();
    }
}
