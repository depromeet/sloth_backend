package com.sloth.api.fcm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenDto {

    @Setter @Getter
    @ApiModel(value = "fcm 토큰 등록 API 요청 객체", description = "fcm 토큰 등록 API 요청 객체")
    public static class Request {

        @ApiModelProperty(value = "fcm token")
        private String fcmToken;

        @ApiModelProperty(value = "기기 device id")
        private String deviceId;

    }

}
