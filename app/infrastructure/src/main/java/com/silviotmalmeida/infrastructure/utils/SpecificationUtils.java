// definição do package
package com.silviotmalmeida.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

// classe auxiliar para a especificação da busca dinâmica
public final class SpecificationUtils {

    // construtuor privado
    private SpecificationUtils(){}

    // método para a especificação da busca dinâmica
    // o atributo prop refere-se ao atributo da entidade a ser considerado na busca
    // o atributo term refere-se à palavra-chave a ser considerada na busca
    public static <T> Specification<T> like(final String prop, final String term){
        return (root, qu, cb) ->
                cb.like(cb.upper(root.get(prop)), "%" + term.toUpperCase() + "%");
    }
}
