// definição do package
package com.silviotmalmeida.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

// interface do jpa repository
// o primeiro argumento é a jpaModel
// o segundo argumento é o tipo do atributo identificador da model
public interface CategoryJpaRepositoryInterface extends JpaRepository<CategoryJpaModel, String> {

    // método de paginação
    Page<CategoryJpaModel> findAll(Specification<CategoryJpaModel> whereClause, Pageable page);
}
