// definição do package
package com.silviotmalmeida.utils;

// classe com métodos úteis para os testes
public class Utils {

    // função que gera uma string randômica de tamanho n
    public static String getAlphaNumericString(int n) {

        // caracteres possíveis
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // criando o buffer de tamanho n
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {

            // sorteando a posição do caractere entre os possíveis
            int index = (int) (AlphaNumericString.length() * Math.random());

            // incrementando o buffer
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
