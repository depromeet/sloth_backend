package com.sloth.api.fcm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "fcm 토큰 등록 API", description = "fcm 토큰 등록 API")
public class FcmTokenDto {

    @Setter @Getter
    public static class Request {

        @ApiModelProperty(value = "fcm token")
        private String fcmToken;

    }

}
