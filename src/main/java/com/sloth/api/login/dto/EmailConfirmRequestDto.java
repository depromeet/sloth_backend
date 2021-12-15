package com.sloth.api.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "이메일 검증 요청 객체", description = "이메일 검증 요청 객체")
public class EmailConfirmRequestDto {
    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "패스워드")
    private String password;

    @ApiModelProperty(value = "검증 코드")
    private String emailConfirmCode;
}
