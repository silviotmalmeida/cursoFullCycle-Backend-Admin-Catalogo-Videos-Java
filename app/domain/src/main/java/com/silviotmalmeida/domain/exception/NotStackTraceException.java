// definição do package
package com.silviotmalmeida.domain.exception;

// classe de exceção que suprime o stacktrace, melhorando a performance
public class NotStackTraceException extends RuntimeException {
    // construtor com message e cause
    public NotStackTraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }

    // construtor só com message
    public NotStackTraceException(final String message) {
        this(message, null);
    }
}
