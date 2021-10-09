package com.sloth.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorMessage {

    private int code;
    private String errorMessage;
    private String referedUrl;

}
