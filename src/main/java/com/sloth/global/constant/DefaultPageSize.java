package com.sloth.global.constant;

import lombok.Getter;

@Getter
public enum DefaultPageSize {

    SIZE_TEN(10)
    ;

    private int size;

    DefaultPageSize(int size) {
        this.size = size;
    }

}
