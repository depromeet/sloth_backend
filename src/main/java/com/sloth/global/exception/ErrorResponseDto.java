package com.sloth.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {

    private int code;
    private String errorMessage;
    private String referedUrl;

}
