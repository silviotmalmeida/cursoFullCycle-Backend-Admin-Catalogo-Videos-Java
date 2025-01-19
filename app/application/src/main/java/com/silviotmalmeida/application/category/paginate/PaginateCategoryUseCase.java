// definição do pacote
package com.silviotmalmeida.application.category.paginate;

import com.silviotmalmeida.application.UseCase;
import com.silviotmalmeida.domain.category.CategorySearchQuery;
import com.silviotmalmeida.domain.pagination.Pagination;

// classe abstrata do usecase
public abstract class PaginateCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<PaginateCategoryOutput>> {
}
