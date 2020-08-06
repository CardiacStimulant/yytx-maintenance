package com.yytx.maintenance.excepion;

public class ResourceManagerException extends RuntimeException {
    public ResourceManagerException() {
        super();
    }

    public ResourceManagerException(String message) {
        super(message);
    }

    public ResourceManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
