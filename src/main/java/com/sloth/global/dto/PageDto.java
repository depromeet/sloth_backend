package com.sloth.global.dto;

import lombok.Getter;

/**
 * 페이지 조회용 공통 dto
 */
@Getter
public class PageDto {

    private int page = 0;
    private int size = 10;

}
