package com.muizz.sajooq.exceptions;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable error) {
        super(message, error);
    }

}
