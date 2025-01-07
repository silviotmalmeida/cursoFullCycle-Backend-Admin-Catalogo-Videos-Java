// definição do pacote
package com.silviotmalmeida.application.category.create;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryID;

// Output DTO para create
public record CreateCategoryOutput(
        Category category
) {
    // factory method
    public static CreateCategoryOutput from(
            final Category category
    ) {
        return new CreateCategoryOutput(category);
    }
}