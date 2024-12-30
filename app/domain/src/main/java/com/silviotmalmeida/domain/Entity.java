// definição do package
package com.silviotmalmeida.domain;

import com.silviotmalmeida.domain.validation.ValidationHandler;

import java.util.Objects;

// classe abstrata a ser estendida pelas entidades
// as entidades precisam receber um identificador
public abstract class Entity<ID extends Identifier> {

    // atributos
    protected final ID id;

    // construtor
    public Entity(final ID id) {
        // validações
        Objects.requireNonNull(id, "'id' should not be null");
        // atribuições
        this.id = id;
    }

    // método de autovalidação
    public abstract void validate(ValidationHandler handler);

    //getters
    public ID getId() {
        return id;
    }

    // sobrescrevendo o equals
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    // sobrescrevendo o hashCode
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
