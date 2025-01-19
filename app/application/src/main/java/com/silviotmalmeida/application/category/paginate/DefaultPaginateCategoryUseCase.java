// definição do pacote
package com.silviotmalmeida.application.category.paginate;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryRepositoryInterface;
import com.silviotmalmeida.domain.category.CategorySearchQuery;
import com.silviotmalmeida.domain.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// implementação do usecase
public class DefaultPaginateCategoryUseCase extends PaginateCategoryUseCase {

    // atributos
    private final CategoryRepositoryInterface repository;

    // construtor
    public DefaultPaginateCategoryUseCase(final CategoryRepositoryInterface repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    // método de execução
    // recebe um input e retorna um output
    @Override
    public Pagination<PaginateCategoryOutput> execute(final CategorySearchQuery input) {

        // obtendo a lista do bd
        Pagination<Category> categories = this.repository.paginate(input);

        // criando a nova lista a partir da conversão dos itens recebidos pelo repository
        List<PaginateCategoryOutput> categoriesOutput = new ArrayList<>();
        for (Category category : categories.items()) {
            categoriesOutput.add(PaginateCategoryOutput.from(category));
        }

        // retornando o output
        return new Pagination<PaginateCategoryOutput>(
                categories.currentPage(),
                categories.perPage(),
                categories.total(),
                categoriesOutput
        );
    }
}
