// definição do package
package com.silviotmalmeida.application.category.find;

import java.time.Instant;

// Output DTO para create
public record FindCategoryOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    // factory method
    public static FindCategoryOutput from(
            final String id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new FindCategoryOutput(id, name, description, isActive, createdAt, updatedAt, deletedAt);
    }
}
