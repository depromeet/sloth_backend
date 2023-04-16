package com.sloth.global.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이지 조회용 공통 dto
 */
@Getter @Setter
public class PageReqeustDto {

    private int page = 0;
    private int size = 10;

}
