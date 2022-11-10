package com.sloth.api.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import com.sloth.api.fcm.dto.FcmTokenDto;
import com.sloth.api.fcm.dto.FcmTokenUpdateDto;
import com.sloth.api.fcm.dto.FindFcmTokenResponseDto;
import com.sloth.domain.fcm.FcmMessage;
import com.sloth.domain.fcm.entity.FcmToken;
import com.sloth.domain.fcm.service.FcmTokenService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.global.exception.BusinessException;
import com.sloth.infra.amazon.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final ObjectMapper objectMapper;
    private final S3Service s3Service;
    private final FcmLessonPushFeignClient fcmLessonPushFeignClient;
    private final MemberService memberService;
    private final FcmTokenService fcmTokenService;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        String result = fcmLessonPushFeignClient.lessonPushAlarm("application/json; UTF-8", "Bearer " + getAccessToken(), message);
        log.info("response : {}", result);
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(s3Service.getThumbnailPath("nanagong_key.json").getObjectContent())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void saveFcmToken(String email, FcmTokenDto.Request request) {
        Member member = memberService.findByEmail(email);

        FcmToken savedFcmToken = fcmTokenService.findByMemberAndDeviceId(member, request.getDeviceId());
        if(savedFcmToken == null) {
            FcmToken newFcmToken = FcmToken.createFcmToken(member, request.getFcmToken(), request.getDeviceId());
            fcmTokenService.saveFcmToken(newFcmToken);
        }

    }

    public void updateFcmTokenUse(String email, FcmTokenUpdateDto.Request request) {
        Member member = memberService.findByEmail(email);
        List<FcmToken> savedFcmTokens = fcmTokenService.findByMember(member);

        if(savedFcmTokens != null && savedFcmTokens.size() != 0) {
            for (FcmToken savedFcmToken : savedFcmTokens) {
                savedFcmToken.updateIsUse(request.getIsUse());
            }
        }
    }

    public FindFcmTokenResponseDto findDeviceFcmToken(String deviceId, String email) {
        Member member = memberService.findByEmail(email);
        FcmToken fcmToken = fcmTokenService.findByMemberAndDeviceId(member, deviceId);
        if(fcmToken == null) {
            return FindFcmTokenResponseDto.builder()
                    .deviceId(deviceId)
                    .build();
        }
        return FindFcmTokenResponseDto.of(fcmToken);
    }
}
