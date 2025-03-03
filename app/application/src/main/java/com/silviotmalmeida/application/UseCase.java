// definição do package
package com.silviotmalmeida.application;

// definindo a classe a ser extendida pelos usecases
// usecase padrão: recebe um input e retorna um output
public abstract class UseCase<INPUT, OUTPUT> {

    //    método de execução
    public abstract OUTPUT execute(INPUT input);
}