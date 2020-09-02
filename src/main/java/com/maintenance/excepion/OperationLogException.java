package com.maintenance.excepion;

public class OperationLogException extends RuntimeException {
    public OperationLogException() {
        super();
    }

    public OperationLogException(String message) {
        super(message);
    }

    public OperationLogException(String message, Throwable cause) {
        super(message, cause);
    }
}
