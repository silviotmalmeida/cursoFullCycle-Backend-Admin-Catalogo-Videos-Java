// definição do pacote
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.CategoryID;

// Input DTO para update
public record UpdateCategoryInput(
        CategoryID id,
        String name,
        String description,
        Boolean isActive
) {
    // factory method
    public static UpdateCategoryInput with(
            final CategoryID id,
            final String name,
            final String description,
            final Boolean isActive
    ) {
        return new UpdateCategoryInput(id, name, description, isActive);
    }
}
