package com.maintenance.excepion;

public class TenantManagerException extends RuntimeException {
    public TenantManagerException() {
        super();
    }

    public TenantManagerException(String message) {
        super(message);
    }

    public TenantManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
