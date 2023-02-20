package com.sloth.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 페이징
    EXCEED_PAGING_SIZE("페이징 사이즈를 초과하였습니다.", "ETC001")
    ;

    ErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }

    private String message;
    private String code;

}
