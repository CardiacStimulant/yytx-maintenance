package com.yytx.maintenance.excepion;

public class ResourceException extends RuntimeException {
    public ResourceException() {
        super();
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
