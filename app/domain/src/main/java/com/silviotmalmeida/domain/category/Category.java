// definição do package
package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.AggregateRoot;

import java.time.Instant;
import java.util.UUID;

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

    // factory method para permitir a contrução dos objetos
    public static Category newCategory(
            final String aName,
            final String aDescription,
            final boolean aIsActive
    ) {
        // criando o id
        final CategoryID id = CategoryID.unique();
        // obtendo o instant da criação
        final Instant now = Instant.now();
        // criando o objeto
        return new Category(id, aName, aDescription, aIsActive, now, now, null);
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