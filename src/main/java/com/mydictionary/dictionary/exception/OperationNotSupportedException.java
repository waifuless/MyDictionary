package com.mydictionary.dictionary.exception;

public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException() {
    }

    public OperationNotSupportedException(String mcg) {
        super(mcg);
    }
}
