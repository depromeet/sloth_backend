package com.sloth.api.category.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@ApiModel(value = "카테고리 조회 객체", description = "카테고리 조회 객체")
public class ResponseCategoryDto {

    private Long categoryId;

    private String categoryName;

}
