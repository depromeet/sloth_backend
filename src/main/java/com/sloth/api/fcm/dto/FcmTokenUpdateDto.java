package com.sloth.api.fcm.dto;

import lombok.*;

@Getter
@Setter
public class FcmTokenUpdateDto {

    @Setter @Getter
    public static class Request {
        private String fcmToken;
        private Boolean isUse;
    }

    @Builder
    @NoArgsConstructor @AllArgsConstructor
    @Setter @Getter
    public static class Response {
        private String fcmToken;
        private Boolean isUse;
    }

}