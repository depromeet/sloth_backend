package com.sloth.api.category.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CategoryDto {

    @Getter @Setter
    @Builder
    @ApiModel(value = "카테고리 조회 객체", description = "카테고리 조회 객체")
    public static class Response {

        @ApiModelProperty(value = "카테고리 아이디")
        private Long categoryId;

        @ApiModelProperty(value = "카테고리명")
        private String categoryName;

    }

}
