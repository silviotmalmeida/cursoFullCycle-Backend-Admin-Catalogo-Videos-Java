// definição do package
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.CategoryID;

// Input DTO para update
public record UpdateCategoryInput(
        String id,
        String name,
        String description,
        Boolean isActive
) {
    // factory method
    public static UpdateCategoryInput with(
            final String id,
            final String name,
            final String description,
            final Boolean isActive
    ) {
        return new UpdateCategoryInput(id, name, description, isActive);
    }
}
