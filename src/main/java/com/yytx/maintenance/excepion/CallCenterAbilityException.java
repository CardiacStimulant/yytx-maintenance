package com.yytx.maintenance.excepion;

public class CallCenterAbilityException extends RuntimeException {
    public CallCenterAbilityException() {
        super();
    }

    public CallCenterAbilityException(String message) {
        super(message);
    }

    public CallCenterAbilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
