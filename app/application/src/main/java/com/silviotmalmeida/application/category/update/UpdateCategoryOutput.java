// definição do pacote
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;

import java.time.Instant;

// Output DTO para update
public record UpdateCategoryOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    // factory method
    public static UpdateCategoryOutput from(
            final String id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new UpdateCategoryOutput(id, name, description, isActive, createdAt, updatedAt, deletedAt);
    }
}
