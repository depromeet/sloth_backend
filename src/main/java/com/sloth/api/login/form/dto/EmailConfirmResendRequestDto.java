package com.sloth.api.login.form.dto;


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
@ApiModel(value = "이메일 검증 재요청 객체", description = "이메일 검증 재요청 객체")
public class EmailConfirmResendRequestDto {
    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "비밀번호")
    private String password;
}
