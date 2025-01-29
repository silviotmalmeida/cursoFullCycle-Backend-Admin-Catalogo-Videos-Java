// definição do pacote
package com.silviotmalmeida.infrastructure.category.persistence;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

// entidade de descrição da persistência
@Entity
// nome da tabela
@Table(name = "categories")
public class CategoryJpaModel {

    // atributo identificador
    @Id
    private String id;

    // coluna name
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    // coluna description
    @Column(name = "description", length = 4000, nullable = true)
    private String description;

    // coluna is_active
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    // coluna created_at
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    // coluna updated_at
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updated_at;

    // coluna deleted_at
    @Column(name = "deleted_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant deleted_at;

    // construtor privado
    private CategoryJpaModel(final String id,
                             final String name,
                             final String description,
                             final boolean isActive,
                             final Instant createdAt,
                             final Instant updated_at,
                             final Instant deleted_at
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    // factory method para criação do model a partir da entidade de domínio
    public static CategoryJpaModel from(final Category category) {
        return new CategoryJpaModel(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }

    // factory method para criação da entidade de domínio a partir do model
    public Category toAggregate() {
        return Category.with(
                CategoryID.from(this.getId()),
                this.getName(),
                this.getDescription(),
                this.isActive(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }

    // getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Instant updated_at) {
        this.updated_at = updated_at;
    }

    public Instant getDeletedAt() {
        return deleted_at;
    }

    public void setDeletedAt(Instant deleted_at) {
        this.deleted_at = deleted_at;
    }
}
