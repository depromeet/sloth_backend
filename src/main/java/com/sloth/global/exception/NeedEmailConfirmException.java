package com.sloth.global.exception;

public class NeedEmailConfirmException extends BusinessException{
    public NeedEmailConfirmException(String message) {
        super(message);
    }
}
