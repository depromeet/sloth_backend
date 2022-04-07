package com.sloth.global.exception;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

public class InvalidParameterException extends IllegalArgumentException {

    public InvalidParameterException(String message) {
        super(message);
    }

    public static void throwErrorMessage(Errors errors) {
        StringBuilder sb = new StringBuilder();
        List<ObjectError> allErrors = errors.getAllErrors();
        for (ObjectError error : allErrors) {
            sb.append(error.getDefaultMessage());
        }
        throw new InvalidParameterException(sb.toString());
    }

}
