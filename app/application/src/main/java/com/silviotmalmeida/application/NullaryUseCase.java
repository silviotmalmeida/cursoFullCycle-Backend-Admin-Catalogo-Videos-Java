// definição do package
package com.silviotmalmeida.application;

// definindo a classe a ser extendida pelos usecases
// usecase padrão: não recebe nada e retorna um output
public abstract class NullaryUseCase<OUTPUT> {

    //    método de execução
    public abstract OUTPUT execute();
}