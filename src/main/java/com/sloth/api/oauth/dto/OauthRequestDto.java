package com.sloth.api.oauth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Getter @Setter
@ApiModel(value = "Oauth 로그인 요청 정보", description = "Oauth 로그인을 위한 요청")
public class OauthRequestDto {

    @ApiModelProperty(value = "access token")
    @NotBlank(message = "access token은 필수 값 입니다.")
    private String accessToken;

    @ApiModelProperty(value = "소셜 로그인 타입(GOOGLE, KAKAO, APPLE)")
    //@NotBlank(message = "소셜 로그인 타입은 필수 입니다.(GOOGLE, KAKAO, APPLE)")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

}
