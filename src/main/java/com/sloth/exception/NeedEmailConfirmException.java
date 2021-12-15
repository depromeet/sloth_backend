package com.sloth.exception;

public class NeedEmailConfirmException extends BusinessException{
    public NeedEmailConfirmException(String message) {
        super(message);
    }
}
