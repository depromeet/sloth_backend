package com.sloth.api.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonParseException;
import com.sloth.api.fcm.dto.FcmTokenUpdateDto;
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
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/test-454ab/messages:send";
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

    public void saveFcmToken(String email, String fcmToken) {
        Member member = memberService.findByEmail(email);

        FcmToken savedFcmToken = fcmTokenService.findByMemberIdAndFcmToken(member.getMemberId(), fcmToken);
        if(savedFcmToken == null) {
            FcmToken newFcmToken = FcmToken.createFcmToken(member, fcmToken);
            fcmTokenService.saveFcmToken(newFcmToken);
        }

    }

    public FcmTokenUpdateDto.Response updateFcmTokenUse(String email, FcmTokenUpdateDto.Request request) {
        Member member = memberService.findByEmail(email);
        FcmToken savedFcmToken = fcmTokenService.findByMemberIdAndFcmToken(member.getMemberId(), request.getFcmToken());
        if(savedFcmToken == null) {
            throw new BusinessException("해당 fcm token이 존재하지 않습니다.");
        } else {
            savedFcmToken.updateIsUse(request.getIsUse());
        }

        return FcmTokenUpdateDto.Response.builder()
                .fcmToken(savedFcmToken.getFcmToken())
                .isUse(savedFcmToken.getIsUse())
                .build();
    }

}
