// definição do package
package com.silviotmalmeida.domain.category;

import com.silviotmalmeida.domain.pagination.Pagination;

import java.util.Optional;

// interface a assinatura dos métodos de acesso à entidade, para uso dos usecases
public interface CategoryGatewayInterface {

    // criação
    Category create(Category category);

    // listagem
    Pagination<Category> paginate(CategorySearchQuery query);

    // busca por id
    Optional<Category> find(CategoryID id);

    // atualização
    Category update(Category category);

    // deleção por id
    boolean delete(CategoryID id);
}
