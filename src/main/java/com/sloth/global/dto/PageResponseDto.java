package com.sloth.global.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class PageResponseDto<T> {
    @ApiModelProperty(value = "총 데이터 수")
    private long totalItems;
    @ApiModelProperty(value = "총 페이지 번호")
    private int totalPages;
    @ApiModelProperty(value = "현재 페이지 번호")
    private int currentPage;
    @ApiModelProperty(value = "페이지당 조회 수")
    private int itemsPerPage;
    @ApiModelProperty(value = "반환 데이터")
    private T data;

}
