package com.muizz.spring.jooq.utils.exceptions;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable error) {
        super(message, error);
    }

}
