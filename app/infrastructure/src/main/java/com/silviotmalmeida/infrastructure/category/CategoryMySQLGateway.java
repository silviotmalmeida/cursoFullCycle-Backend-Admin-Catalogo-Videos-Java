// definição do package
package com.silviotmalmeida.infrastructure.category;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategorySearchQuery;
import com.silviotmalmeida.domain.pagination.Pagination;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaModel;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaRepositoryInterface;
import org.springframework.stereotype.Service;

import java.util.Optional;

// marcando como serviço
@Service
// classe que implementa o CategoryGatewayInterface do domínio para o jpa/mysql
public class CategoryMySQLGateway implements CategoryGatewayInterface {

    // declarando o repository
    private final CategoryJpaRepositoryInterface repository;

    // construtor
    public CategoryMySQLGateway(final CategoryJpaRepositoryInterface repository) {
        this.repository = repository;
    }

    // criação
    @Override
    public Category create(final Category category) {
        return save(category);
    }

    // listagem
    @Override
    public Pagination<Category> paginate(CategorySearchQuery query) {
        return null;
    }

    // busca por id
    @Override
    public Optional<Category> find(CategoryID id) {
        return Optional.empty();
    }

    // atualização
    @Override
    public Category update(final Category category) {
        return save(category);
    }

    // deleção por id
    @Override
    public boolean delete(CategoryID id) {
        return false;
    }

    // como no jpa o create e update são iguais, foi criado um método privado único
    private Category save(final Category category) {
        // criando a model de entrada
        final CategoryJpaModel inputModel = CategoryJpaModel.from(category);
        // salvando no BD e obtendo a model de saída
        final CategoryJpaModel outputModel = this.repository.save(inputModel);
        // retornando a entidade gerada a partir dos dados persistidos no BD
        return outputModel.toAggregate();
    }
}
