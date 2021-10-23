package com.sloth.api.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "폼 로그인 요청 객체", description = "폼 로그인을 위한 요청 객체")
public class FormJoinDto {

    @ApiModelProperty(value = "멤버 이름")
    private String memberName;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "비밀번호")
    private String password;

    @ApiModelProperty(value = "비밀번호 확인")
    private String passwordConfirm;

}