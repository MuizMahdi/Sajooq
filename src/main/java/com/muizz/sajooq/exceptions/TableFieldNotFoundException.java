package com.muizz.sajooq.exceptions;

public class TableFieldNotFoundException extends RuntimeException {

    public TableFieldNotFoundException(String message) {
        super(message);
    }

    public TableFieldNotFoundException(String message, Throwable error) {
        super(message, error);
    }

}
