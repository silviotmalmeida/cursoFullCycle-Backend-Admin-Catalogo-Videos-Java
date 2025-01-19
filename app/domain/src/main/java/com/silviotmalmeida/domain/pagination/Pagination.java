// definição do pacote
package com.silviotmalmeida.domain.pagination;

import java.util.List;
import java.util.function.Function;

// responsável pela definição das regras de paginação da listagem
public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {
    // método responsável pela transformação dos itens
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        // convertendo a lista original em outra baseada na função recebida
        final List<R> newItems = this.items().stream().map(mapper).toList();
        // retornando
        return new Pagination<>(currentPage(), perPage(), total(), newItems);
    }
}
