// definição do pacote
package com.silviotmalmeida.application.category.paginate;

import com.silviotmalmeida.domain.category.Category;

import java.time.Instant;

// Output DTO para find
public record PaginateCategoryOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    // factory method
    public static PaginateCategoryOutput from(
            final Category category
    ) {
        return new PaginateCategoryOutput(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt());
    }
}
