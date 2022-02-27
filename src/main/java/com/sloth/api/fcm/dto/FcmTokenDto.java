package com.sloth.api.fcm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenDto {

    @Setter @Getter
    public static class Request {
        private String fcmToken;
    }

}
