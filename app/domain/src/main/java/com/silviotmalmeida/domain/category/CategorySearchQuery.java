// definição do pacote
package com.silviotmalmeida.domain.category;

// responsável por reunir os critérios de busca na listagem
public record CategorySearchQuery(
        int page,
        int perPage,
        String terms,
        String sortField,
        String direction
) {
}
