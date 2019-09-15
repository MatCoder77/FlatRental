package com.flatrental.infrastructure.exceptions;

public class ResourceStorageException extends RuntimeException {

    public ResourceStorageException(String message) {
        super(message);
    }

    public ResourceStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}