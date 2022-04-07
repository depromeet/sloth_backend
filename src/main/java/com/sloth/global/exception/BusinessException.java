package com.sloth.global.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int code;

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

}
