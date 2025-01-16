// definição do pacote
package com.silviotmalmeida.application.category.find;

import com.silviotmalmeida.domain.category.CategoryID;

// Input DTO para find
public record FindCategoryInput(
        CategoryID id
) {
    // factory method
    public static FindCategoryInput with(
            final CategoryID id
    ) {
        return new FindCategoryInput(id);
    }
}
