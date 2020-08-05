package com.yytx.maintenance.excepion;

public class RoleManagerException extends RuntimeException {
    public RoleManagerException() {
        super();
    }

    public RoleManagerException(String message) {
        super(message);
    }

    public RoleManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
