package com.sloth.api.fcm.dto;

import com.sloth.domain.fcm.entity.FcmToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class FindFcmTokenResponseDto {

    private String deviceId;
    private String fcmToken;

    public static FindFcmTokenResponseDto of(FcmToken fcmToken) {
        return FindFcmTokenResponseDto.builder()
                .deviceId(fcmToken.getDeviceId())
                .fcmToken(fcmToken.getFcmToken())
                .build();
    }


}
