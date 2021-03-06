package com.sloth.api.fcm.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(url = "https://fcm.googleapis.com", name = "FcmLessonPushFeignClient")
public interface FcmLessonPushFeignClient {

    @PostMapping(value = "/v1/projects/nanagong-336615/messages:send", produces = "application/json", consumes = "application/json")
    String lessonPushAlarm(
            @RequestHeader("Content-type") String contentType,
            @RequestHeader("Authorization") String accessToken,
            @RequestBody String message
    );
}
