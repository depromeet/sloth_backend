package com.sloth.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    // token 에러
    REFRESH_TOKEN_NOT_FOUND("해당 리프레시 토큰이 존재하지 않습니다.");
    private String message;

}
