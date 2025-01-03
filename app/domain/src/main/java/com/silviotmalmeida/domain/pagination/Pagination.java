// definição do pacote
package com.silviotmalmeida.domain.pagination;

import java.util.List;

// responsável pela definição das regras de paginação da listagem
public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {
}
