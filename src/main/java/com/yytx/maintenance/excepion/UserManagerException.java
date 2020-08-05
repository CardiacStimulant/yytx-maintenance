package com.yytx.maintenance.excepion;

public class UserManagerException extends RuntimeException {
    public UserManagerException() {
        super();
    }

    public UserManagerException(String message) {
        super(message);
    }

    public UserManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
