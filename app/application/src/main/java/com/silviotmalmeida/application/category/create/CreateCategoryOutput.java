// definição do package
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;

import java.time.Instant;

// Output DTO para create
public record CreateCategoryOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    // factory method
    public static CreateCategoryOutput from(
            final String id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new CreateCategoryOutput(id, name, description, isActive, createdAt, updatedAt, deletedAt);
    }
}
