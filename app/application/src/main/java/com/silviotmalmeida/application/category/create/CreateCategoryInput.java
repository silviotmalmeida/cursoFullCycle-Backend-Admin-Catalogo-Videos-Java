// definição do pacote
package com.silviotmalmeida.application.category.create;

// Input DTO para create
public record CreateCategoryInput(
        String name,
        String description,
        boolean isActive
) {
    // factory method
    public static CreateCategoryInput with(
            final String name,
            final String description,
            final boolean isActive
    ) {
        return new CreateCategoryInput(name, description, isActive);
    }
}
