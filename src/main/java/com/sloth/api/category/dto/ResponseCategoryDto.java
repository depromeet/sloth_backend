package com.sloth.api.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ResponseCategoryDto {

    private Long categoryId;

    private String categroyName;

}
