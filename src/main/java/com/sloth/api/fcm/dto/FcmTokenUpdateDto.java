package com.sloth.api.fcm.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
public class FcmTokenUpdateDto {

    @Setter @Getter
    @ApiModel(value = "fcm 알림 사용여부 업데이트 API 요청 객체", description = "fcm 알림 사용여부 업데이트 API 요청 객체")
    public static class Request {
        private String fcmToken;
        private Boolean isUse;
    }

    @Builder
    @NoArgsConstructor @AllArgsConstructor
    @Setter @Getter
    @ApiModel(value = "fcm 알림 사용여부 업데이트 API 반환 객체", description = "fcm 알림 사용여부 업데이트 API 반환 객체")
    public static class Response {
        private String fcmToken;
        private Boolean isUse;
    }

}