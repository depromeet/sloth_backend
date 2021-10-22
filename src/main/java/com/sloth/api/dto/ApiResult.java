package com.sloth.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "처리 결과 반환 객체", description = "처리 결과 반환 객체")
public class ApiResult {

    @ApiModelProperty(value = "처리 결과 코드")
    private String code;

    @ApiModelProperty(value = "처리 결과 메세지")
    private String message;

}
