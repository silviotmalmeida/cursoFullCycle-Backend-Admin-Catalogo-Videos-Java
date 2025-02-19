// definição do package
package com.silviotmalmeida.infrastructure.category;

import com.silviotmalmeida.domain.category.Category;
import com.silviotmalmeida.domain.category.CategoryGatewayInterface;
import com.silviotmalmeida.domain.category.CategoryID;
import com.silviotmalmeida.domain.category.CategorySearchQuery;
import com.silviotmalmeida.domain.pagination.Pagination;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaModel;
import com.silviotmalmeida.infrastructure.category.persistence.CategoryJpaRepositoryInterface;
import com.silviotmalmeida.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    public Pagination<Category> paginate(final CategorySearchQuery query) {

        // organizando os parâmetros da paginação
        final PageRequest pageRequest = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sortField())
        );

        // organizando a especificação da busca dinâmica
        // se o parâmetro terms existir considera-o
        // senão retorna null
        // estão sendo considerados os atributos name e description na busca
        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    final Specification<CategoryJpaModel> nameLike = SpecificationUtils.like("name", str);
                    final Specification<CategoryJpaModel> descriptionLike = SpecificationUtils.like("description", str);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);

        // realizando a busca
        final Page<CategoryJpaModel> page = this.repository.findAll(Specification.where(specifications), pageRequest);

        // retornando o pagination
        return new Pagination<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.map(CategoryJpaModel::toAggregate).stream().toList()
        );
    }

    // busca por id
    @Override
    public Optional<Category> find(CategoryID id) {
        return this.repository.findById(id.getValue()).map(CategoryJpaModel::toAggregate);
    }

    // atualização
    @Override
    public Category update(final Category category) {
        return save(category);
    }

    // deleção por id
    @Override
    public boolean delete(CategoryID id) {
        // se o id existir
        if (this.repository.existsById(id.getValue())) {
            // remove o registro
            this.repository.deleteById(id.getValue());
            // retorna true
            return true;
        }
        // senão
        else {
            // retorna false
            return false;
        }
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
