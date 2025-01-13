// definição do package
package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.AggregateRoot;
import com.silviotmalmeida.domain.validation.ValidationHandler;

import java.time.Instant;

// definindo a entidade
public class Category extends AggregateRoot<CategoryID> {

    // atributos
    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    // construtor privado
    private Category(
            final CategoryID id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // factory method para permitir a contrução dos objetos novos
    public static Category newCategory(
            final String name,
            final String description,
            final boolean isActive
    ) {
        // criando o id
        final CategoryID id = CategoryID.unique();
        // obtendo o instant da criação
        final Instant now = Instant.now();
        // definindo o deletedAt
        final Instant deletedAt = isActive ? null : now;
        // criando o objeto
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    // factory method para permitir a contrução dos objetos já com id definido
    public static Category newCategory(
            final CategoryID id,
            final String name,
            final String description,
            final boolean isActive
    ) {
        // obtendo o instant da criação
        final Instant now = Instant.now();
        // definindo o deletedAt
        final Instant deletedAt = isActive ? null : now;
        // criando o objeto
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    // método de autovalidação
    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    // método de desativação
    public Category deactivate() {
        this.isActive = false;
        Instant now = Instant.now();
        this.updatedAt = now;
        if (this.getDeletedAt() == null) {
            this.deletedAt = now;
        }
        return this;
    }

    // método de ativação
    public Category activate() {
        this.isActive = true;
        Instant now = Instant.now();
        this.updatedAt = now;
        if (this.getDeletedAt() != null) {
            this.deletedAt = null;
        }
        return this;
    }

    // método de atualização geral
    public Category update(final String name, final String description, final boolean isActive) {
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        return this;
    }

    // getters
    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}