// definição do package
package com.silviotmalmeida.domain;

// classe abstrata a ser estendida pelos agregados
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    // construtor
    public AggregateRoot(ID id) {
        super(id);
    }
}
