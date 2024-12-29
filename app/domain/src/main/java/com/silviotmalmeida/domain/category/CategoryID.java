// definição do package
package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

// classe que identifica uma categoria
public class CategoryID extends Identifier {

    // atributos
    private final String value;

    // construtor privado
    private CategoryID(final String value) {
        // validações
        Objects.requireNonNull(value);

        // atribuições
        this.value = value;
    }

    // factory methods para permitir a contrução dos objetos
    // objeto novo
    public static CategoryID unique() {
        return new CategoryID(UUID.randomUUID().toString().toLowerCase());
    }

    // objeto a partir de um ID existente
    public static CategoryID from(final String id) {
        return new CategoryID(id);
    }

    // objeto a partir de um UUID existente
    public static CategoryID from(final UUID id) {
        return new CategoryID(id.toString().toLowerCase());
    }

    // getters
    public String getValue() {
        return value;
    }

    // sobrescrevendo o equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    // sobrescrevendo o hashCode
    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
