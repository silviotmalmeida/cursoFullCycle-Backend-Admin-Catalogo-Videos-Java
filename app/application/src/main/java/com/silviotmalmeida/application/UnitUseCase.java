// definição do package
package com.silviotmalmeida.application;

// definindo a classe a ser extendida pelos usecases
// usecase padrão: recebe um input e não retorna nada
public abstract class UnitUseCase<INPUT> {

    //    método de execução
    public abstract void execute(INPUT input);
}