// definição do pacote
package com.silviotmalmeida.application.category.delete;

import com.silviotmalmeida.domain.category.CategoryID;

// Input DTO para update
public record DeleteCategoryInput(
        CategoryID id
) {
    // factory method
    public static DeleteCategoryInput with(
            final CategoryID id
    ) {
        return new DeleteCategoryInput(id);
    }
}
