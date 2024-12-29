// definição do package
package com.silviotmalmeida.application;

import com.silviotmalmeida.domain.category.Category;

// definindo a entidade
public class UseCase {

    //    métodos
    public Category execute() {
        return Category.newCategory("name", "description", false);
    }
}