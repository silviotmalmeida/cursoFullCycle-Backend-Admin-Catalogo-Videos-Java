// definição do pacote
package com.silviotmalmeida.application.category.update;

import com.silviotmalmeida.domain.category.Category;

// Output DTO para update
public record UpdateCategoryOutput(
        Category category
) {
    // factory method
    public static UpdateCategoryOutput from(
            final Category category
    ) {
        return new UpdateCategoryOutput(category);
    }
}
