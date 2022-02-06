package com.sloth.api.fcm.dto;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "푸시 알림 요청 API", description = "푸시 알림 요청 API")
public class FcmDto {

    @Getter @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @ApiModelProperty(value = "디바이스 토큰")
        private String targetToken;

        @ApiModelProperty(value = "푸시 알림 타이틀")
        private String title;

        @ApiModelProperty(value = "푸시 알림 내용")
        private String body;
    }
}