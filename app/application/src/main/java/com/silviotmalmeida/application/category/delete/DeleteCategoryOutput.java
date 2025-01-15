// definição do pacote
package com.silviotmalmeida.application.category.delete;

import com.silviotmalmeida.domain.category.Category;

// Output DTO para update
public record DeleteCategoryOutput(
        boolean success
) {
    // factory method
    public static DeleteCategoryOutput from(
            final boolean success
    ) {
        return new DeleteCategoryOutput(success);
    }
}
